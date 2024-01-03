// https://stackoverflow.com/questions/25801755/multiple-column-sorting-jqgrid

var lastsel3; //選擇一行進行編輯時用到的
$(function() {
    $("#list").jqGrid({
        url: 'studentShow_do.jsp',
        datatype: 'json',
        mtype: 'post',
        colNames: ['學號', '姓名', '年齡', '生日'],
        colModel: [{
                name: 'sid',
                index: 'sid',
                width: 155,
                align: 'center',
                editable: true
            },
            //edittype 修改時設定編輯框的HTML樣式的
            {
                name: 'sname',
                index: 'sname',
                width: 190,
                align: 'center',
                editable: true,
                edittype: 'select',
                editoptions: {
                    value: "1:張三;2:李四;3:王五"
                }
            },
            //行內編輯時自定義的驗證valiAge方法中寫驗證條件 
            {
                name: 'age',
                index: 'age',
                width: 180,
                align: 'center',
                editable: true,
                editrules: {
                    custom: true,
                    custom_func: valiAge
                }
            },
            /**對於時間格式的顯示formatter:'date'格式化時間和formatoptions:{srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'} 配合使用,
             * H:i:s用於顯示時分秒,newformat: 'Y-m-d',當Y為小寫時 只顯示年份的後兩位如1988則顯示88;m(月),d(日)為大寫時則顯示英文(簡寫)的月日;
             */
            {
                name: 'brith',
                index: 'brith',
                formatter: 'date',
                formatoptions: {
                    srcformat: 'Y-m-d',
                    newformat: 'Y-m-d'
                },
                width: 250,
                align: 'center',
                editable: true,
                sorttype: "date"
            }
        ],
        //forceFit : true,//調整列寬度
        pager: '#pager', //定義頁碼控制條Page Bar,需要一個div
        rownumbers: true, //如果為ture則會在表格左邊新增一列，顯示行順序號，從1開始遞增(翻頁後的值與rowList有關)。此列名為'rn'.
        rowNum: 5, //一頁中顯示的最大條數,返回條數大於此值時,仍顯示此值的條數.
        rowList: [20, 50, 100], //一個數組，用於設定Grid可以接受的rowNum值
        sortname: 'sid', //初始根據哪個列來排序, 當在表格中點選不同的列頭,可以實現動態根據某列來排序,將會把點選的列名傳到後臺來實現根據不同的欄位來排序
        sortable: true,
        viewrecords: true, //設定是否在Pager Bar顯示所有記錄的總數
        sortorder: 'asc', //排序asc or desc
        prmNames: {
            page: 'page',
            rows: 'rows',
            totalrows: 'totalrows',
            sort: "sidx"
        }, //設定預設傳到後臺的引數名稱
        loadtext: '等等',
        height: '80%',
        altRow: true,
        multiselect: true, //是否顯示多選框
        multiboxonly: false, //是否只有點選多選框時,才執行選擇多選框checkbox.預設為false,點選一行亦選定此行的多選框
        multiselectWidth: 50, //多選框所在列的寬度
        page: 1, //初始顯示第幾頁
        //cellEdit: true,//與colModel中editable屬性配合使用,當editable:true,cellEdit:true時可以對editable:true的列進行單個單元格編輯,cellEdit:false時(colModel中所有列editable都為true時)整行編輯
        editurl: 'ok.jsp', //編輯欄編輯後傳送的地址(整行)
        cellurl: 'ok.jsp', //單元格的傳送地址
        gridview: true, //構造一行資料後新增到grid中，如果設為true則是將整個表格的資料都構造完成後再新增到grid中，但treeGrid, subGrid, or afterInsertRow 不能用
        subGrid: true, //開啟顯示子表的按鈕
        caption: "jqGrid test",
        subGridRowExpanded: function(subgrid_id, row_id) { //顯示子表
            var subgrid_table_id = "subgrid_" + subgrid_id;
            $("#" + subgrid_id).html("<table id='" + subgrid_table_id + "'></table>");
            $("#" + subgrid_table_id).jqGrid({
                url: 'studentShow_do.jsp',
                datatype: 'json',
                colNames: ['學號', '姓名', '年齡', '生日'],
                colModel: [{
                        name: 'sid',
                        index: 'sid',
                        width: 155,
                        align: 'center'
                    },
                    {
                        name: 'sname',
                        index: 'sname',
                        width: 190,
                        align: 'center'
                    },
                    {
                        name: 'age',
                        index: 'age',
                        width: 180,
                        align: 'center'
                    },
                    {
                        name: 'brith',
                        index: 'brith',
                        width: 250,
                        sortable: false
                    }
                ],
                height: '100%'
            });
        },

        onSelectRow: function(rowid, sid, status) {
            if (rowid && rowid !== lastsel3) { //當選中的不是上一次選中的行就(restoreRow)釋放(恢復)上一次選中的行,避免選中多行
                jQuery('#list').jqGrid('restoreRow', lastsel3);
                jQuery('#list').jqGrid('editRow', rowid, true, pickdates);
                lastsel3 = rowid;
            } else {
                jQuery('#list').jqGrid('editRow', rowid, true, pickdates);
                lastsel3 = rowid;
            }
        },
        loadComplete: function() {
            //alert("s");
        }
    });
    //多重表頭
    //頂級表頭
    jQuery("#list").jqGrid('setGroupHeaders', {
        useColSpanStyle: false, //沒有表頭的列是否與表頭列位置的空單元格合併
        groupHeaders: [
            //合併 startColumnName(開始列),以sid列開始; numberOfColumns(合併幾列),合併2列; titleText(合併後父列名),合併後父列名為All Student
            {
                startColumnName: 'sid',
                numberOfColumns: 4,
                titleText: 'All Student Info'
            }
        ]
    });
    //二級表頭
    jQuery("#list").jqGrid('setGroupHeaders', {
        useColSpanStyle: false, //沒有表頭的列是否與表頭列位置的空單元格合併
        groupHeaders: [
            //合併 startColumnName(開始列),以sid列開始; numberOfColumns(合併幾列),合併2列; titleText(合併後父列名),合併後父列名為All Student
            {
                startColumnName: 'sid',
                numberOfColumns: 3,
                titleText: 'Base Student Info'
            },
            {
                startColumnName: 'brith',
                numberOfColumns: 2,
                titleText: 'other'
            }
        ]
    });
    jQuery("#list").jqGrid('navGrid', '#pager', {
        edit: true,
        add: true,
        del: true
    });
    //$("#list").jqGrid('inlineNav','#pager');//行內編輯 (或與單元格編輯(選擇) 引衝突)
    //對話方塊
    $("#dialog").dialog({
        autoOpen: false,
        show: "blind",
        hide: "explode"
    });
});

function pickdates(rowid) {
    $("#" + rowid + "_brith", "#list").datepicker({
        dateFormat: "yy-mm-dd"
    }); //時間控制元件
}

function valiAge(value, colname) {
    if (value < 0 || value > 120) {
        return [false, "請填寫0-120之間的數字!"];
    } else {
        return [true, "ok"];
    }
}