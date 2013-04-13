for(var i = 0; i < 248; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

});

widgetIdToShowFunction['u223'] = function() {
var e = windowEvent;

if (true) {

	BringToFront("u223");

}

}

widgetIdToShowFunction['u12'] = function() {
var e = windowEvent;

if (true) {

	BringToFront("u12");

	BringToFront("u111");

	SetPanelVisibility('u111','','none',500);

}

}

widgetIdToHideFunction['u12'] = function() {
var e = windowEvent;

if (true) {

	SetPanelVisibility('u106','hidden','none',500);

	SetPanelVisibility('u111','hidden','none',500);

}

}

widgetIdToShowFunction['u179'] = function() {
var e = windowEvent;

if (true) {

	BringToFront("u179");

}

}

widgetIdToHideFunction['u179'] = function() {
var e = windowEvent;

if (true) {

}

}

widgetIdToShowFunction['u106'] = function() {
var e = windowEvent;

if (true) {

	BringToFront("u106");

	SetPanelVisibility('u12','','none',500);

}

}

widgetIdToHideFunction['u106'] = function() {
var e = windowEvent;

if (true) {

	SetPanelVisibility('u12','hidden','none',500);

}

}

widgetIdToShowFunction['u2'] = function() {
var e = windowEvent;

if (true) {

	BringToFront("u2");
function waitufec36100abc44313b8ad02bccb48e2f91() {

	SetPanelVisibility('u2','hidden','none',500);
}
setTimeout(waitufec36100abc44313b8ad02bccb48e2f91, 1000);

}

}

function rdo0SwitchApps(e) {

}

function rdo0Home(e) {

}

function rdo0Back(e) {

}

function rdo0OK(e) {

}

function rdo0Cancel(e) {

}
gv_vAlignTable['u122'] = 'center';gv_vAlignTable['u32'] = 'center';gv_vAlignTable['u243'] = 'center';gv_vAlignTable['u130'] = 'top';document.getElementById('u236_img').tabIndex = 0;
HookClick('u236', false);

u236.style.cursor = 'pointer';
$axure.eventManager.click('u236', function(e) {

if ((GetCheckState('u238')) == (true)) {

SetCheckState('u238', false);

	SetPanelStateNext('u206',true,'none','',500,'none','',500);

}
else
if (true) {

SetCheckState('u238', true);

	SetPanelStateNext('u206',true,'none','',500,'none','',500);

}
});
gv_vAlignTable['u4'] = 'center';u140.tabIndex = 0;

u140.style.cursor = 'pointer';
$axure.eventManager.click('u140', function(e) {

if (true) {

	SetPanelVisibility('u114','hidden','none',500);

}
});

$axure.eventManager.keyup('u222', function(e) {

if (true) {

SetWidgetFormText('u214', GetWidgetText('u222'));

}
});

$axure.eventManager.focus('u222', function(e) {

if (true) {

	SetPanelState('u215', 'pd1u215','none','',500,'none','',500);

	SetPanelVisibility('u12','','none',500);

}

if ((GetWidgetText('u222')) == ('Password')) {

SetWidgetFormText('u222', '');

SetWidgetFormText('u214', '');

}

if ((GetCheckState('u238')) == (false)) {

	SetPanelState('u206', 'pd1u206','none','',500,'none','',500);

	var obj1 = document.getElementById("u214");
    obj1.focus();

}
});

$axure.eventManager.blur('u222', function(e) {

if (true) {

	SetPanelState('u215', 'pd0u215','none','',500,'none','',500);

}

if ((GetWidgetText('u222')) == ('')) {

SetWidgetFormText('u222', 'Password');

SetWidgetFormText('u214', 'Password');

}
});
gv_vAlignTable['u151'] = 'center';gv_vAlignTable['u42'] = 'center';gv_vAlignTable['u229'] = 'center';u101.tabIndex = 0;

u101.style.cursor = 'pointer';
$axure.eventManager.click('u101', function(e) {

if (true) {

rdo0SwitchApps(e);

}
});
gv_vAlignTable['u14'] = 'center';gv_vAlignTable['u48'] = 'center';gv_vAlignTable['u105'] = 'center';gv_vAlignTable['u235'] = 'center';gv_vAlignTable['u138'] = 'center';gv_vAlignTable['u52'] = 'center';gv_vAlignTable['u20'] = 'center';gv_vAlignTable['u120'] = 'center';gv_vAlignTable['u110'] = 'center';gv_vAlignTable['u6'] = 'center';
$axure.eventManager.focus('u205', function(e) {

if (true) {

	SetPanelState('u198', 'pd1u198','none','',500,'none','',500);

	SetPanelVisibility('u12','','none',500);

}

if ((GetWidgetText('u205')) == ('E-mail')) {

SetWidgetFormText('u205', '');

}
});

$axure.eventManager.blur('u205', function(e) {

if (true) {

	SetPanelState('u198', 'pd0u198','none','',500,'none','',500);

}

if ((GetWidgetText('u205')) == ('')) {

SetWidgetFormText('u205', 'E-mail');

}
});
gv_vAlignTable['u108'] = 'center';gv_vAlignTable['u247'] = 'center';
u238.style.cursor = 'pointer';
$axure.eventManager.click('u238', function(e) {

if (true) {

	SetPanelStateNext('u206',true,'none','',500,'none','',500);

}
});
gv_vAlignTable['u62'] = 'center';gv_vAlignTable['u11'] = 'center';gv_vAlignTable['u34'] = 'center';gv_vAlignTable['u68'] = 'center';gv_vAlignTable['u185'] = 'center';gv_vAlignTable['u72'] = 'center';gv_vAlignTable['u103'] = 'top';u99.tabIndex = 0;

u99.style.cursor = 'pointer';
$axure.eventManager.click('u99', function(e) {

if (true) {

rdo0Back(e);

}
});
gv_vAlignTable['u233'] = 'center';gv_vAlignTable['u66'] = 'center';u112.tabIndex = 0;

u112.style.cursor = 'pointer';
$axure.eventManager.click('u112', function(e) {

if (true) {

	SetPanelVisibility('u12','hidden','none',500);

}
});
gv_vAlignTable['u44'] = 'center';gv_vAlignTable['u78'] = 'center';gv_vAlignTable['u231'] = 'center';gv_vAlignTable['u191'] = 'top';gv_vAlignTable['u16'] = 'center';gv_vAlignTable['u125'] = 'top';gv_vAlignTable['u172'] = 'center';document.getElementById('u246_img').tabIndex = 0;
HookClick('u246', false);

u246.style.cursor = 'pointer';
$axure.eventManager.click('u246', function(e) {

if (true) {

SetGlobalVariableValue('OnLoadVariable', GetWidgetText('u205'));

SetWidgetRichText('u195', '<p style="text-align:left;"><span style="font-family:Roboto;font-size:14px;font-weight:normal;font-style:normal;text-decoration:none;color:#000000;">An e</span><span style="font-family:Roboto;font-size:14px;font-weight:normal;font-style:normal;text-decoration:none;color:#000000;">-</span><span style="font-family:Roboto;font-size:14px;font-weight:normal;font-style:normal;text-decoration:none;color:#000000;">mail with verification link has been sent to</span><span style="font-family:Roboto;font-size:14px;font-weight:normal;font-style:normal;text-decoration:none;color:#000000;"> ' + (GetGlobalVariableValue('OnLoadVariable')) + '</span></p>');

	SetPanelVisibility('u179','','none',500);

}
});
gv_vAlignTable['u149'] = 'center';gv_vAlignTable['u54'] = 'center';gv_vAlignTable['u118'] = 'center';gv_vAlignTable['u88'] = 'center';gv_vAlignTable['u189'] = 'center';gv_vAlignTable['u38'] = 'center';gv_vAlignTable['u176'] = 'center';gv_vAlignTable['u26'] = 'center';gv_vAlignTable['u174'] = 'center';gv_vAlignTable['u128'] = 'center';u100.tabIndex = 0;

u100.style.cursor = 'pointer';
$axure.eventManager.click('u100', function(e) {

if (true) {

rdo0Home(e);

}
});
gv_vAlignTable['u82'] = 'center';gv_vAlignTable['u36'] = 'center';gv_vAlignTable['u30'] = 'center';gv_vAlignTable['u195'] = 'top';gv_vAlignTable['u116'] = 'center';gv_vAlignTable['u158'] = 'center';gv_vAlignTable['u74'] = 'center';gv_vAlignTable['u156'] = 'center';gv_vAlignTable['u160'] = 'center';document.getElementById('u166_img').tabIndex = 0;

u166.style.cursor = 'pointer';
$axure.eventManager.click('u166', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Not_logged_in.html');

}
});
gv_vAlignTable['u92'] = 'center';gv_vAlignTable['u46'] = 'center';gv_vAlignTable['u126'] = 'top';gv_vAlignTable['u181'] = 'center';u98.tabIndex = 0;

u98.style.cursor = 'pointer';
$axure.eventManager.click('u98', function(e) {

if (true) {

}
});

$axure.eventManager.keyup('u214', function(e) {

if (true) {

SetWidgetFormText('u222', GetWidgetText('u214'));

}
});

$axure.eventManager.focus('u214', function(e) {

if (true) {

	SetPanelState('u207', 'pd1u207','none','',500,'none','',500);

	SetPanelVisibility('u12','','none',500);

}

if ((GetWidgetText('u214')) == ('Password')) {

SetWidgetFormText('u214', '');

}
});

$axure.eventManager.blur('u214', function(e) {

if (true) {

	SetPanelState('u207', 'pd0u207','none','',500,'none','',500);

}

if ((GetWidgetText('u214')) == ('')) {

SetWidgetFormText('u214', 'Password');

SetWidgetFormText('u222', 'Password');

	SetPanelState('u206', 'pd0u206','none','',500,'none','',500);

}
});
gv_vAlignTable['u225'] = 'center';gv_vAlignTable['u169'] = 'center';gv_vAlignTable['u56'] = 'center';gv_vAlignTable['u187'] = 'center';gv_vAlignTable['u142'] = 'center';gv_vAlignTable['u154'] = 'center';gv_vAlignTable['u40'] = 'center';gv_vAlignTable['u227'] = 'center';u139.tabIndex = 0;

u139.style.cursor = 'pointer';
$axure.eventManager.click('u139', function(e) {

if (true) {

	SetPanelVisibility('u114','hidden','none',500);

}
});
gv_vAlignTable['u193'] = 'center';document.getElementById('u192_img').tabIndex = 0;
HookClick('u192', false);

u192.style.cursor = 'pointer';
$axure.eventManager.click('u192', function(e) {

if (true) {

	SetPanelVisibility('u179','hidden','none',500);

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Profile.html');

}
});
document.getElementById('u242_img').tabIndex = 0;
HookClick('u242', false);

u242.style.cursor = 'pointer';
$axure.eventManager.click('u242', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Profile.html');

}
});
gv_vAlignTable['u164'] = 'center';document.getElementById('u109_img').tabIndex = 0;

u109.style.cursor = 'pointer';
$axure.eventManager.click('u109', function(e) {

if (true) {

	SetPanelVisibility('u106','hidden','none',500);

rdo0OK(e);

}
});
gv_vAlignTable['u84'] = 'center';gv_vAlignTable['u50'] = 'center';gv_vAlignTable['u239'] = 'top';gv_vAlignTable['u170'] = 'top';gv_vAlignTable['u76'] = 'center';gv_vAlignTable['u134'] = 'center';document.getElementById('u228_img').tabIndex = 0;
HookClick('u228', false);

u228.style.cursor = 'pointer';
$axure.eventManager.click('u228', function(e) {

if (true) {

	SetPanelVisibility('u223','hidden','none',500);

}
});
document.getElementById('u177_img').tabIndex = 0;

u177.style.cursor = 'pointer';
$axure.eventManager.click('u177', function(e) {

if (true) {

	SetPanelVisibility('u223','toggle','none',500);

}
});
gv_vAlignTable['u94'] = 'center';gv_vAlignTable['u60'] = 'center';gv_vAlignTable['u9'] = 'center';document.getElementById('u234_img').tabIndex = 0;
HookClick('u234', false);

u234.style.cursor = 'pointer';
$axure.eventManager.click('u234', function(e) {

if (true) {

	SetPanelVisibility('u223','hidden','none',500);

}
});
gv_vAlignTable['u64'] = 'center';gv_vAlignTable['u70'] = 'center';gv_vAlignTable['u24'] = 'center';document.getElementById('u230_img').tabIndex = 0;
HookClick('u230', false);

u230.style.cursor = 'pointer';
$axure.eventManager.click('u230', function(e) {

if (true) {

	SetPanelVisibility('u223','hidden','none',500);

}
});
gv_vAlignTable['u162'] = 'center';gv_vAlignTable['u132'] = 'center';gv_vAlignTable['u129'] = 'top';gv_vAlignTable['u58'] = 'center';gv_vAlignTable['u183'] = 'center';document.getElementById('u232_img').tabIndex = 0;
HookClick('u232', false);

u232.style.cursor = 'pointer';
$axure.eventManager.click('u232', function(e) {

if (true) {

	SetPanelVisibility('u223','hidden','none',500);

}
});
gv_vAlignTable['u178'] = 'center';document.getElementById('u8_img').tabIndex = 0;

u8.style.cursor = 'pointer';
$axure.eventManager.click('u8', function(e) {

if (true) {

	SetPanelVisibility('u12','hidden','none',500);

}
});
gv_vAlignTable['u96'] = 'center';document.getElementById('u15_img').tabIndex = 0;

u15.style.cursor = 'pointer';
$axure.eventManager.click('u15', function(e) {

if (true) {

SetFocusedWidgetText('[[LVAR1 + \'q\']]');

}
});
gv_vAlignTable['u124'] = 'center';gv_vAlignTable['u80'] = 'center';gv_vAlignTable['u1'] = 'center';gv_vAlignTable['u167'] = 'center';gv_vAlignTable['u145'] = 'center';gv_vAlignTable['u237'] = 'center';gv_vAlignTable['u90'] = 'center';gv_vAlignTable['u18'] = 'center';gv_vAlignTable['u22'] = 'center';u143.tabIndex = 0;

u143.style.cursor = 'pointer';
$axure.eventManager.click('u143', function(e) {

if (true) {

	BringToFront("u114");

	SetPanelVisibility('u114','toggle','none',500);

}
});
document.getElementById('u107_img').tabIndex = 0;

u107.style.cursor = 'pointer';
$axure.eventManager.click('u107', function(e) {

if (true) {

	SetPanelVisibility('u106','hidden','none',500);

rdo0Cancel(e);

}
});
gv_vAlignTable['u136'] = 'center';document.getElementById('u180_img').tabIndex = 0;

u180.style.cursor = 'pointer';
$axure.eventManager.click('u180', function(e) {

if (true) {

	SetPanelVisibility('u179','hidden','none',500);

}
});
gv_vAlignTable['u28'] = 'center';