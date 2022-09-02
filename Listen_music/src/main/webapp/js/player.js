function render(album) {
  if (album.trackList.length === 0) {
    alert('专辑中一首音频都没有')
    return
  }
  
  const oWrapper = document.querySelector('.wrapper')
  const oCover = document.querySelector('.cover')
  const oTitle = document.querySelector('.name')
  const oArtist = document.querySelector('.artist')
  const oAudio = document.querySelector('#main-audio')
  const playPauseBtn = document.querySelector('.play-pause')
  const prevBtn = document.querySelector('#prev')
  const nextBtn = document.querySelector('#next')
  const oProgressBar = document.querySelector('.progress-bar')
  const oProgressArea = document.querySelector('.progress-area')
  const oCurrent = document.querySelector('.current')
  const oDuration = document.querySelector('.duration')
  const oMusicList = document.querySelector('.music-list')
  const closeBtn = document.querySelector('#close')

  // 当前哪首歌
  let index = 0
  let track = album.trackList[index]
  oCover.src = album.cover
  oArtist.textContent = album.username

  function reRender(track) {
    oTitle.textContent = `${album.title} - ${track.title}`
    oAudio.src = `/track?tid=${track.tid}`
  }

  reRender(track)

  oAudio.addEventListener('loadeddata', function () {
    let audioDuration = oAudio.duration
    let durationMin = Math.floor(audioDuration / 60)
    let durationSec = Math.floor(audioDuration % 60)
    if (durationSec < 10) {
      durationSec = '0' + durationSec
    }
    oDuration.textContent = `${durationMin}:${durationSec}`
  })

  oAudio.addEventListener('timeupdate', function (e) {
    const current = e.target.currentTime
    const duration = e.target.duration
    let progressWidth = (current / duration) * 100
    if (!isNaN(progressWidth)) {
      oProgressBar.style.width = progressWidth + '%'
    }

    let currentMin = Math.floor(current / 60)
    let currentSec = Math.floor(current % 60)
    if (currentSec < 10) {
      currentSec = '0' + currentSec
    }
    oCurrent.textContent = `${currentMin}:${currentSec}`
  })

  oProgressArea.addEventListener('click', function (e) {
    let progressWidth = oProgressArea.clientWidth
    let clickedOffsetX = e.offsetX
    let duration = oAudio.duration
    oAudio.currentTime = (clickedOffsetX / progressWidth) * duration
    play()
  })

  oAudio.addEventListener('ended', function () {
    next()
  })
  
  function play() {
    oWrapper.classList.add('playing')
    playPauseBtn.querySelector('i').className = 'fas fa-pause'
    oAudio.play()
  }

  function pause() {
    oWrapper.classList.remove('playing')
    playPauseBtn.querySelector('i').className = 'fas fa-play'
    oAudio.pause()
  }
  
  function prev() {
    index--
    if (index === -1) {
      index = album.trackList.length - 1
    }
    track = album.trackList[index]
    reRender(track)
    play()
  }

  function next() {
    index++
    if (index === album.trackList.length) {
      index = 0
    }
    track = album.trackList[index]
    reRender(track)
    play()
  }

  prevBtn.addEventListener('click', function () {
    prev()
  })

  nextBtn.addEventListener('click', function () {
    next()
  })

  playPauseBtn.addEventListener('click', function () {
    if (oWrapper.classList.contains('playing')) {
      pause()
    } else {
      play()
    }
  })
}

window.addEventListener('load', function () {
  let aid = getParameter('aid')
  if (isNaN(aid)) {
    alert("没有携带 aid 或者 aid 不是数字")
    return
  }
  let xhr = new XMLHttpRequest()
  xhr.open('get', '/album-detail.json?aid=' + aid)
  xhr.addEventListener('load', function () {
    console.log(this.responseText)
    let data = JSON.parse(this.responseText)
    render(data)
  })
  xhr.send()
})