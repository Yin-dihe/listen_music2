window.onload = function() {
    var xhr = new XMLHttpRequest()
    xhr.open('get', '/studio/album/list.json')
    xhr.onload = function() {
        var data = JSON.parse(xhr.responseText)

        var oTable = document.querySelector('table')
        var list = data.albumList
        for (var i in list) {
            var a = list[i]
            var anchor
            if (a.state === '已发布') {
                anchor = `<a href="/studio/album/withdraw.do?aid=${a.aid}">下线</a>`
            } else {
                anchor = `<a href="/studio/album/publish.do?aid=${a.aid}">发布</a>`
            }
            var html = `<tr><td>${a.aid}</td><td>${a.title}</td><td><img src="${a.cover}"></td><td>${a.state}</td><td><a href="/studio/album/bind.html?aid=${a.aid}">绑定</a></td><td>${anchor}</td></tr>`
            oTable.innerHTML += html
        }
    }
    xhr.send()
}