function render(trackList) {
    // 1. 找到用插入的容器元素
    var tbody = document.querySelector('tbody')

    // 2. 遍历音频列表
    for (var i in trackList) {
        var track = trackList[i]        // { tid: ..., title: ... }
        var html = `<tr><td>${track.tid}</td><td>${track.title}</td><td>${track.refCount}</td></tr>`
        // 3. 添加每个音频的 html，到容器元素中
        tbody.innerHTML += html
    }
}

function renderNoLogin() {
    document.querySelector('.who').textContent = '必须登录后才能使用'
}

function renderWho(user) {
    document.querySelector('.who').textContent = user.username
}

function renderPagination(pagination) {
    var currentPage = parseInt(pagination.currentPage)
    if (currentPage === 1) {
        // 说明没有上一页
        document.querySelector('.prevPage').href += currentPage
    } else {
        document.querySelector('.prevPage').href += (currentPage - 1)
    }
    document.querySelector('.countPerPage').textContent = pagination.countPerPage
    document.querySelector('.currentPage').textContent = pagination.currentPage
    document.querySelector('.totalPage').textContent = pagination.totalPage
    if (currentPage >= parseInt(pagination.totalPage)) {
        document.querySelector('.nextPage').href += currentPage
    } else {
        document.querySelector('.nextPage').href += (currentPage + 1)
    }
    document.querySelector('.lastPage').href += pagination.totalPage
}

// 1. 为了让 js 代码可以在所有资源都加载完成后才去执行，就入口代码放在 window 的 load 事件处理中
window.onload = function() {
    // 执行这个方法的时候，说明所有资源都被加载好了

    // 1. 发起 ajax 请求
    var xhr = new XMLHttpRequest()
    xhr.open('get', '/studio/track/list.json' + location.search)
    xhr.onload = function() {
        if (xhr.status !== 200) {
            alert('快去检查下吧，后端 JSON 出错了')
            return
        }
        // 2. 增加调试信息
        console.log(this.responseText)
        // 3. 尝试使用 JSON 进行解析（如果后端出现了 500 错误，则响应不是 JSON，这里就会错误）
        var data = JSON.parse(this.responseText)
        // 4. 判断是否正确（用户是否登录了），根据 currentUser 的值
        if (!data.currentUser) {
            // 没有登录
            renderNoLogin()
            return
        }

        renderWho(data.currentUser)
        renderPagination(data.pagination)
        render(data.trackList)  // { "currentUser": ..., "trackList": [ ... ] }
    }
    xhr.send()      // 对 JSON 的请求是由这里发起的
}