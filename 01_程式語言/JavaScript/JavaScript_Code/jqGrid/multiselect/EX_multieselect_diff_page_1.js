// jqGrid remains selected when using multiselect to turn pages
// 翻頁後 紀錄已選取的列表


//Set the array and current page number selected by the global variable
var checkArray = [];
var pageNum = 1;
/**
 * Initialize DataGrid
 */
function initDataGrid() {
  $('#grid-table').jqGrid({
    url: $.cxt + "/coWarnArea/getAreaPeopleSettingList",
    datatype: "json",
    postData: {
      warnType: '4',
    },
    mtype: "POST",
    height: 'auto',
    autowidth: true,
    colNames: ['Case No', 'Case name', 'City of ownership', 'Case level', 'start time', 'End time'],
    colModel: [{
      name: 'warnAreaId',
      align: 'center',
      index: 'warnAreaId',
      editable: false,
    }, {
      name: 'warnAreaName',
      align: 'center',
      index: 'warnAreaName',
      editable: false,
      formatter: renderOperation
    }, {
      name: 'areaName',
      align: 'center',
      index: 'areaName',
      editable: false
    }, {
      name: 'caseLevel',
      align: 'center',
      index: 'caseLevel',
      editable: false
    }, {
      name: 'startDate',
      align: 'center',
      index: 'startDate',
      editable: false
    }, {
      name: 'endDate',
      align: 'center',
      index: 'endDate',
      search: false,
      sortable: false,
      editable: false,
    }],
    viewrecords: true,
    rowNum: 10,
    rowList: [10, 20, 30],
    pager: '#grid-pager',
    altRows: true,

    multiselect: true, //Add this property to display the selection checkbox
    //multiboxonly : false,
    onSelectAll: function (rowIds, status) {
      //Triggered when all is selected. rowIds are all rowIds of the page
      //Delete all pages in checkArray array
      for (var i = 0; i < rowIds.length; i++) {
        for (var j = 0; j < checkArray.length; j++) {
          if (checkArray[j].pageNum == pageNum && checkArray[j].rowId == rowIds[i]) {
            checkArray.splice(j, 1);
            break;
          }
        }
      }
      //If it is selected, add all items on the current page
      if (status) {
        for (var i = 0; i < rowIds.length; i++) {
          var rowData = $("#grid-table").jqGrid('getRowData', rowIds[i]);
          var checkedItem = {
            "pageNum": pageNum,
            "rowId": rowIds[i],
            "ID": rowData.warnAreaId
          };
          checkArray.push(checkedItem)
        }
      }
      //console.log(checkArray)
    },
    onSelectRow: function (rowId, status) {
      //Click checkbox to trigger rowId as the currently triggered rowId
      //Row data of the selected row
      var rowData = $("#grid-table").jqGrid('getRowData', rowId);
      var checkedItem = {
        "pageNum": pageNum,
        "rowId": rowId,
        "ID": rowData.warnAreaId
      };
      if (status) {
        //If it is selected, the array will be added if there is no one in the array
        for (var i = 0; i < checkArray.length; i++) {
          if (checkArray[i].pageNum == pageNum && checkArray[i].rowId == rowId) {
            return false;
          }
        }
        checkArray.push(checkedItem);
      }
      else {
        //Delete array
        for (var i = 0; i < checkArray.length; i++) {
          if (checkArray[i].pageNum == pageNum && checkArray[i].rowId == rowId) {
            checkArray.splice(i, 1);
            break;
          }
        }
      }
      //console.log(checkArray)
    },

    //Triggered when each page loads
    loadComplete: function (data) {
      //Change pageNum page number of global variable
      pageNum = data.page;
      //Loop array to make the rowId in the page selected
      for (var i = 0; i < checkArray.length; i++) {
        if (checkArray[i].pageNum == pageNum) {
          $("#grid-table").jqGrid('setSelection', checkArray[i].rowId, true);
        }
      }
      //console.log(checkArray)
    },
  });
  // JqGrid re layout
  $(window).triggerHandler('resize.jqGrid');
  // JqGrid page bar settings
  $('#grid-table').jqGrid('navGrid', '#grid-pager', {
    edit: false,
    editicon: 'ace-icon fa fa-pencil blue',
    add: false,
    addicon: 'ace-icon fa fa-plus-circle purple',
    del: false,
    delicon: 'ace-icon fa fa-trash-o red',
    search: false,
    searchicon: 'ace-icon fa fa-search orange',
    refresh: true,
    refreshicon: 'ace-icon fa fa-refresh green',
    view: false,
    viewicon: 'ace-icon fa fa-search-plus grey'
  })
}