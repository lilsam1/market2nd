
let arrID = new Array();

window.onload = function init() {
    with(frmName()) {
        if (chkdID.value != "") {
            arrID =  chkdID.value.split(",");
            chkAll.checked = true;
        }
    }
    setChkAllYN();
}

let setChkAllYN = function() { // 전체가 선택되었는지 검사
    with(frmName()) {
        let totalCntForm = totalCntFormChecked = 0;
        for (var i = 0; i < elements.length; i++) {
            var e = elements[i];
            if((e.type === 'checkbox') && (e.name === 'chkID')) {
                totalCntForm++;
                if (e.checked) {
                    totalCntFormChecked++;
                }
            }
        }

        if (totalCntFormChecked === totalCntForm) {
            chkAll.checked=true;
        }
        else {
            chkAll.checked=false;
        }
    }
}

let setArrChange = function (flag, ID) {
    var idx = null;

    for ( i = 0; i < arrID.length; i++) {
        if(arrID[i] === ID) {
            idx = i;
        }
    }

    if (idx != null) {
        arrID.splice(idx,1);
    }

    if (flag) {
        arrID.push(ID);
    }

    frmName().chkdID.value = arrID;
}

let setChkAll = function() { //전체 선택
    with(frmName()) {
        for (var i = 0; i < elements.length; i++) {
            var e = elements[i];

            if((e.type === 'checkbox') && (e.name === 'chkID')) {
                e.checked = chkAll.checked;
                if (chkAll.checked) {
                    setArrChange(true, e.value);
                }
                else {
                    setArrChange(false, e.value);
                }
            }
        }
    }
}

let setChkAlone = function(T) { //개별 선택
    with(frmName()) {
        if (T.checked) {
            setArrChange(true, T.value);
        }
        else {
            setArrChange(false, T.value);
        }
        setChkAllYN();
    }
}

