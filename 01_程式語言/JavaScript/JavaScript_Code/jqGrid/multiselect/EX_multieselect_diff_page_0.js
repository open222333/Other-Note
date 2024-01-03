function setJQGrid() {
  $("#jqGrid").jqGrid({
    url: '/xxx/role/queryPage',
    datatype: "json",
    colModel: [{
        label: '角色序號',
        name: 'roleId',
        sortable: true
      },
      {
        label: '角色名',
        name: 'name',
        sortable: true
      },
      {
        label: '創建人',
        name: 'createBy',
        sortable: true
      },
      {
        label: '創建時間',
        name: 'createDate',
        sortable: true
      },
      {
        label: '最近更新人',
        name: 'updateBy',
        sortable: true
      },
      {
        label: '最近更新時間',
        name: 'updateDate',
        sortable: true
      },
      {
        label: '備註',
        name: 'remark',
        sortable: true
      },
      {
        label: '操作',
        name: 'operate',
        sortable: false,
        formatter: function(cellvalue, options, rowObject) {
          return "<a onclick='edit(" + options.rowId + ")' style='cursor: pointer;'>編輯</a>    <a onclick='deleteRoleRow(" + options.rowId + ")' style='cursor: pointer;'>刪除</a>";
        }
      }
    ],
    shrinkToFit: true,
    autoScroll: true,
    viewrecords: true,
    height: 365,
    rowNum: 3,
    rowList: [3, 30, 50],
    rownumbers: true,
    pgbuttons: true,
    pginput: true,
    //autowidth:true, 
    width: 1314,
    //rownumWidth:2000,
    multiselect: true, //複選框
    pager: "#jqGridPager",
    jsonReader: {
      root: "roles",
      page: "page.currPage",
      total: "page.totalPage",
      records: "page.totalCount"
    },
    prmNames: {
      page: "currPage",
      rows: "limit",
      order: "order",
    },
    //當表格所有數據都加載完成而且其他的處理也都完成時觸發此事件，排序，翻頁同樣也會觸發此事件
    gridComplete: function() {
      //凍結列
      jQuery("#jqGrid").jqGrid('setFrozenColumns');
      //對jqgrid的序號進行列名的設置
      jQuery("#jqGrid").jqGrid('setLabel', 0, '標記', 'labelstyle');
      //獲取所有行rowid
      var ids = jQuery("#jqGrid").jqGrid('getDataIDs');
      if (ids) {
        //遍歷所有id
        for (var i = 0; i < ids.length; i++) {
          //選中偶數行
          if (ids[i] % 2 == 0) {
            //對偶數行進行class設置
            $('#jqGrid ' + '#' + ids[i]).find("td").addClass("table-list-bg-color");
          }
        }
      };
      //1 判斷selectsMap是否爲空
      if (selectsMap.size > 0) {
        //2 獲取翻頁之後的當前頁碼
        var currentPage = $('#jqGrid').getGridParam('page');
        //3 對selectsMap進行遍歷
        selectsMap.forEach(function(value, key, map) {
          //4 判斷翻頁之後的當前頁是痘有多選框被勾選過
          if (value.page == currentPage) {
            //5 已經被勾選過就進行保持選中
            $("#jqGrid").setSelection(value.id);
          }
        })
      }
    },
    //當選擇行時觸發此事件 rowid：當前行id；status：選擇狀態
    onSelectRow: function(rowid, status) {
      //1、選中某一行時將當前頁的頁數和選中的id放入對象中
      //1.1 獲取當前頁碼
      var page = $('#jqGrid').getGridParam('page');
      //1.2 獲取當前被勾選的所在行所有數據
      var rowData = $('#jqGrid').jqGrid('getRowData', rowid);
      //1.3 判斷是選中狀態還是取消狀態
      if (status) {
        //1.4 獲取當前行的對應所有頁碼的唯一數據
        var roleId = rowData.roleId;
        //1.5 將頁碼以及行id放入到緩存對象中去
        var selecttest = new selects(page, rowid);
        //1.6 將所有頁面中唯一id作爲key，將對象作爲value存入緩存map中
        selectsMap.set(roleId, selecttest);
      } else {
        //如果是取消狀態就從selectsMap中刪除這一緩存
        selectsMap.delete(rowData.roleId);
      };
      //頁面上的已選幾條記錄
      //獲取selectsMap的長度賦值給vue的selectRows判斷當前已勾選多少條記錄
      vm.selectRows = selectsMap.size;
    },
    //選中所有行
    onSelectAll: function() {
      var ids = $("#jqGrid").jqGrid("getGridParam", "selarrrow");
      if (ids != null) {
        vm.selectRows = ids.length;
      };
    }
  });
}