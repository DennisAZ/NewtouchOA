
function isValidDatetime(str) {
  if (!str) {
    return;
  }
  var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{2}):(\d{1,2}):(\d{2})$/); 
  if (r == null) {
    return false;
  }
  if (parseInt(r[1]) > 9999 || parseInt(r[1]) < 1753) {
    return false;
  }
  var d = new Date(r[1], r[3]-1, r[4], r[5], r[6], r[7]); 
  return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4] &&
      d.getHours() == r[5] && d.getMinutes() == r[6] && d.getSeconds() == r[7]);
}