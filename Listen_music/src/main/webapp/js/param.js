function getParameter(name) {
  let s = location.search;
  if (s.trim().length > 0) {
    s = s.substr(1);
    let parts = s.split('&');
    for (let i in parts) {
      let part = parts[i];
      let kv = part.split('=');
      let key = decodeURIComponent(kv[0]);
      let value = undefined;
      if (kv.length > 1) {
        value = decodeURIComponent(kv[1]);
      }

      if (name === key) {
        return value;
      }
    }
  }

  return undefined;
}