window.onload = function() {
    var xhr = new XMLHttpRequest()
    xhr.open('get', '/studio/album/candidate.json' + location.search)
    xhr.onload = function() {
        var data = JSON.parse(xhr.responseText)

        document.querySelector('#aid').value = data.aid

        var oContainer = document.querySelector('.container')
        var list = data.trackList
        for (var i in list) {
            var t = list[i]

            var html = `<div><input type="checkbox" name="add-tid" value="${t.tid}">${t.title}</div>`

            oContainer.innerHTML += html
        }
    }
    xhr.send()
}