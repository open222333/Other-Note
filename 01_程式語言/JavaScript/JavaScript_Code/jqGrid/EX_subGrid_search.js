function onSearch() {
  $("#jqGridExplore").jqGrid('setGridParam', {
    search: true,
    postData: {
      "filters": $(this).jqGrid("getGridParam", "postData").filters,
      "subgrid": true
    }
  }).trigger("reloadGrid");

  return false;
}