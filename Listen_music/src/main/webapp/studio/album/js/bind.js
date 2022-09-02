window.onload = function() {
    var xhr = new XMLHttpRequest()
    xhr.open('get', '/studio/album/bind.json' + location.search)
    xhr.onload = function() {
        var data = JSON.parse(xhr.responseText)

        document.querySelector('.add').href += location.search
        document.querySelector('.title').textContent = data.title

        var oContainer = document.querySelector('.trackList')
        var list = data.trackList
        for (var i in list) {
            var t = list[i]

            var html = `<div>${t.title}</div>`

            oContainer.innerHTML += html
        }
    }
    xhr.send()
}