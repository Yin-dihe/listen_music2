window.onload = function() {
    var xhr = new XMLHttpRequest()
    xhr.open('get', '/album-list.json')
    xhr.onload = function() {
        var data = JSON.parse(this.responseText)
        var list = data.albumList
        var oContainer = document.querySelector('.container')
        for (var i in list) {
            var a = list[i]
            var html = `<div><div><img src="${a.cover}"></div><h1><a href="/player.html?aid=${a.aid}">${a.title}</a></h1><p>${a.username}</p></div>`
            oContainer.innerHTML += html
        }
    }
    xhr.send()
}