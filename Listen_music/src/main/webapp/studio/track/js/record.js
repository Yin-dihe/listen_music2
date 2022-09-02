function upload(title, content) {
    var xhr = new XMLHttpRequest()
    // 由于我们要把声音放在请求体中发送，所以必须使用 post 方法
    // 这里是 ajax 发起的请求，所以，后续要怎么处理完全是 js 决定的
    // 所以，这里就不使用 *.do 这种 URL 了
    xhr.open('post', `/studio/track/record.json?title=${title}`)
    xhr.onload = function() {
        console.log(this.status)
        console.log(this.responseText)
    }
    xhr.send(content)
}

window.onload = function() {
    if (!navigator.mediaDevices.getUserMedia) {
        alert('浏览器太古老了，不支持媒体访问，请更新浏览器')
        return
    }

    var constraints = {
        audio: true
    }
    var promise = navigator.mediaDevices.getUserMedia(constraints)

    // stream 的类型就是 MediaStream (媒体流)
    // 里面有从麦克风采集到的声音
    function onSuccess(stream) {
        alert('用户授权成功')

        var mediaRecorder = new MediaRecorder(stream)
        // 当开始录制后，这个方法会被调用
        mediaRecorder.onstart = function () {
            console.log('开始录制')
        }

        // 放置过程中录制下来的声音数据
        var data = []
        // 当数据可用时，会被调用
        mediaRecorder.ondataavailable = function(evt) {
            console.log('数据可用')
            console.log(evt)
            // evt.data 是一个 Blob 类型的，表示录制下来的声音片段的对象
            data.push(evt.data)
        }
        // 当停止录制后（一切完成），这个方法会被调用
        mediaRecorder.onstop = function() {
            console.log('停止录制')
            console.log(data)
            // 将一组数据，组成一个统一的数据
            var title = document.querySelector('#title').value.trim()
            var type = "audio/ogg; codecs=opus"
            var blob = new Blob(data, {
                // 类型认为是死规定
                type: type
            })
            // 将数据生成一个方便播放的 URL
            var url = URL.createObjectURL(blob)
            console.log(url)

            // 找到 audio 元素，修改 src 属性，让页面上的
            // 音频播放器可以播放我们录制下来的声音
            var oAudio = document.querySelector('audio')
            oAudio.src = url

            upload(title, blob)
        }

        document.querySelector('#stop').onclick = function() {
            mediaRecorder.stop()
        }

        // 开始录制，每1s生成一次数据
        mediaRecorder.start(1000)


//        // 10s 之后，执行这个函数
//        setTimeout(function () {
//            // 10秒后停止录制
//            mediaRecorder.stop();
//        }, 10*1000)
    }

    function onError(error) {
        alert('授权过程中出现了错误: ' + error)
    }

    promise.then(onSuccess).catch(onError)
}