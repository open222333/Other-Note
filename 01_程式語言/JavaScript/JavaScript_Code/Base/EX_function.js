function get_repository_name(path) {
  /**
   * 取得路徑最後的名稱
   */
  let s = path.split('/');
  return s[s.length - 1];
}