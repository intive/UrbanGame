for(var i = 0; i < 275; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

});

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

widgetIdToShowFunction['u144'] = function() {
var e = windowEvent;

if (true) {

	BringToFront("u144");

}

}

widgetIdToHideFunction['u144'] = function() {
var e = windowEvent;

if (true) {

}

}

widgetIdToShowFunction['u217'] = function() {
var e = windowEvent;

if (true) {

	SetPanelVisibility('u220','hidden','none',500);

}

}

widgetIdToShowFunction['u220'] = function() {
var e = windowEvent;

if (true) {

	SetPanelVisibility('u217','hidden','none',500);

}

}

widgetIdToShowFunction['u262'] = function() {
var e = windowEvent;

if (true) {

	BringToFront("u262");

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
gv_vAlignTable['u115'] = 'center';gv_vAlignTable['u32'] = 'center';gv_vAlignTable['u252'] = 'top';gv_vAlignTable['u156'] = 'top';gv_vAlignTable['u207'] = 'center';gv_vAlignTable['u236'] = 'center';gv_vAlignTable['u4'] = 'center';gv_vAlignTable['u226'] = 'top';gv_vAlignTable['u222'] = 'center';gv_vAlignTable['u135'] = 'center';gv_vAlignTable['u42'] = 'center';gv_vAlignTable['u256'] = 'center';HookClick('u229', false);
u101.tabIndex = 0;

u101.style.cursor = 'pointer';
$axure.eventManager.click('u101', function(e) {

if (true) {

rdo0SwitchApps(e);

}
});
gv_vAlignTable['u186'] = 'center';gv_vAlignTable['u14'] = 'center';gv_vAlignTable['u48'] = 'center';gv_vAlignTable['u105'] = 'center';document.getElementById('u235_img').tabIndex = 0;
HookClick('u235', false);

u235.style.cursor = 'pointer';
$axure.eventManager.click('u235', function(e) {

if (true) {

if (IsWidgetSelected('u235')) {
SetWidgetNotSelected('u235'); } else {
SetWidgetSelected('u235');
}
}
else
if ((GetCheckState('u237')) == (true)) {

SetCheckState('u237', false);

}
else
if (true) {

SetCheckState('u237', true);

}
});
gv_vAlignTable['u268'] = 'center';gv_vAlignTable['u52'] = 'center';gv_vAlignTable['u20'] = 'center';gv_vAlignTable['u152'] = 'center';gv_vAlignTable['u110'] = 'center';gv_vAlignTable['u6'] = 'center';gv_vAlignTable['u205'] = 'center';gv_vAlignTable['u108'] = 'center';document.getElementById('u247_img').tabIndex = 0;
HookClick('u247', false);

u247.style.cursor = 'pointer';
$axure.eventManager.click('u247', function(e) {

if (true) {

if (IsWidgetSelected('u247')) {
SetWidgetNotSelected('u247'); } else {
SetWidgetSelected('u247');
}
}
else
if ((GetCheckState('u249')) == (true)) {

SetCheckState('u249', false);

}
else
if (true) {

SetCheckState('u249', true);

}
});
gv_vAlignTable['u238'] = 'top';
u245.style.cursor = 'pointer';
$axure.eventManager.click('u245', function(e) {

if (true) {

if (IsWidgetSelected('u243')) {
SetWidgetNotSelected('u243'); } else {
SetWidgetSelected('u243');
}
}
});
gv_vAlignTable['u62'] = 'center';gv_vAlignTable['u141'] = 'center';gv_vAlignTable['u11'] = 'center';gv_vAlignTable['u133'] = 'top';gv_vAlignTable['u200'] = 'center';gv_vAlignTable['u34'] = 'center';gv_vAlignTable['u68'] = 'center';gv_vAlignTable['u266'] = 'center';gv_vAlignTable['u213'] = 'center';gv_vAlignTable['u184'] = 'center';gv_vAlignTable['u264'] = 'center';gv_vAlignTable['u72'] = 'center';gv_vAlignTable['u103'] = 'top';u99.tabIndex = 0;

u99.style.cursor = 'pointer';
$axure.eventManager.click('u99', function(e) {

if (true) {

rdo0Back(e);

}
});

u233.style.cursor = 'pointer';
$axure.eventManager.click('u233', function(e) {

if (true) {

if (IsWidgetSelected('u231')) {
SetWidgetNotSelected('u231'); } else {
SetWidgetSelected('u231');
}
}
});
gv_vAlignTable['u66'] = 'center';
u249.style.cursor = 'pointer';
$axure.eventManager.click('u249', function(e) {

if (true) {

if (IsWidgetSelected('u247')) {
SetWidgetNotSelected('u247'); } else {
SetWidgetSelected('u247');
}
}
});
u112.tabIndex = 0;

u112.style.cursor = 'pointer';
$axure.eventManager.click('u112', function(e) {

if (true) {

	SetPanelVisibility('u12','hidden','none',500);

}
});
gv_vAlignTable['u44'] = 'center';gv_vAlignTable['u78'] = 'center';gv_vAlignTable['u119'] = 'center';gv_vAlignTable['u16'] = 'center';gv_vAlignTable['u125'] = 'center';gv_vAlignTable['u246'] = 'top';gv_vAlignTable['u54'] = 'center';document.getElementById('u231_img').tabIndex = 0;
HookClick('u231', false);

u231.style.cursor = 'pointer';
$axure.eventManager.click('u231', function(e) {

if (true) {

if (IsWidgetSelected('u231')) {
SetWidgetNotSelected('u231'); } else {
SetWidgetSelected('u231');
}
}
else
if ((GetCheckState('u233')) == (true)) {

SetCheckState('u233', false);

}
else
if (true) {

SetCheckState('u233', true);

}
});
gv_vAlignTable['u88'] = 'center';gv_vAlignTable['u189'] = 'top';gv_vAlignTable['u38'] = 'center';gv_vAlignTable['u176'] = 'center';document.getElementById('u267_img').tabIndex = 0;
HookClick('u267', false);

u267.style.cursor = 'pointer';
$axure.eventManager.click('u267', function(e) {

if (true) {

	SetPanelVisibility('u262','hidden','none',500);

}
});
gv_vAlignTable['u26'] = 'center';gv_vAlignTable['u128'] = 'top';document.getElementById('u191_img').tabIndex = 0;

u191.style.cursor = 'pointer';
$axure.eventManager.click('u191', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Active_game.html');

}
});
gv_vAlignTable['u258'] = 'center';
u241.style.cursor = 'pointer';
$axure.eventManager.click('u241', function(e) {

if (true) {

if (IsWidgetSelected('u239')) {
SetWidgetNotSelected('u239'); } else {
SetWidgetSelected('u239');
}
}
});
document.getElementById('u243_img').tabIndex = 0;
HookClick('u243', false);

u243.style.cursor = 'pointer';
$axure.eventManager.click('u243', function(e) {

if (true) {

if (IsWidgetSelected('u243')) {
SetWidgetNotSelected('u243'); } else {
SetWidgetSelected('u243');
}
}
else
if ((GetCheckState('u245')) == (true)) {

SetCheckState('u245', false);

}
else
if (true) {

SetCheckState('u245', true);

}
});
u100.tabIndex = 0;

u100.style.cursor = 'pointer';
$axure.eventManager.click('u100', function(e) {

if (true) {

rdo0Home(e);

}
});
gv_vAlignTable['u202'] = 'center';gv_vAlignTable['u82'] = 'center';gv_vAlignTable['u36'] = 'center';gv_vAlignTable['u30'] = 'center';gv_vAlignTable['u219'] = 'center';u116.tabIndex = 0;

u116.style.cursor = 'pointer';
$axure.eventManager.click('u116', function(e) {

if (true) {

}
});
gv_vAlignTable['u158'] = 'center';gv_vAlignTable['u74'] = 'center';gv_vAlignTable['u123'] = 'center';u223.tabIndex = 0;

u223.style.cursor = 'pointer';
$axure.eventManager.click('u223', function(e) {

if (true) {

	SetPanelVisibility('u217','','none',500);

SetWidgetSelected('u212');
	SetPanelState('u225', 'pd0u225','none','',500,'none','',500);
function waitu61cada313a3f4c65992e9d124fb036121() {

SetWidgetNotSelected('u212');}
setTimeout(waitu61cada313a3f4c65992e9d124fb036121, 200);

}
});
document.getElementById('u157_img').tabIndex = 0;
HookClick('u157', false);

u157.style.cursor = 'pointer';
$axure.eventManager.click('u157', function(e) {

if (true) {

	SetPanelVisibility('u144','hidden','none',500);

}
});
gv_vAlignTable['u92'] = 'center';gv_vAlignTable['u46'] = 'center';gv_vAlignTable['u198'] = 'center';u98.tabIndex = 0;

u98.style.cursor = 'pointer';
$axure.eventManager.click('u98', function(e) {

if (true) {

}
});
gv_vAlignTable['u127'] = 'center';HookClick('u257', false);
gv_vAlignTable['u240'] = 'center';gv_vAlignTable['u56'] = 'center';gv_vAlignTable['u150'] = 'center';u142.tabIndex = 0;

u142.style.cursor = 'pointer';
$axure.eventManager.click('u142', function(e) {

if (true) {

}
});

$axure.eventManager.focus('u168', function(e) {

if (true) {

	SetPanelState('u161', 'pd1u161','none','',500,'none','',500);

}

if ((GetWidgetText('u168')) == ('Report task')) {

SetWidgetFormText('u168', '');

}
});

$axure.eventManager.blur('u168', function(e) {

if (true) {

	SetPanelState('u161', 'pd0u161','none','',500,'none','',500);

}

if ((GetWidgetText('u168')) == ('')) {

SetWidgetFormText('u168', 'Report task');

}
});
gv_vAlignTable['u154'] = 'center';gv_vAlignTable['u40'] = 'center';gv_vAlignTable['u139'] = 'center';document.getElementById('u269_img').tabIndex = 0;
HookClick('u269', false);

u269.style.cursor = 'pointer';
$axure.eventManager.click('u269', function(e) {

if (true) {

	SetPanelVisibility('u262','hidden','none',500);

}
});
gv_vAlignTable['u192'] = 'center';gv_vAlignTable['u121'] = 'center';gv_vAlignTable['u250'] = 'top';document.getElementById('u109_img').tabIndex = 0;

u109.style.cursor = 'pointer';
$axure.eventManager.click('u109', function(e) {

if (true) {

	SetPanelVisibility('u106','hidden','none',500);

rdo0OK(e);

}
});
gv_vAlignTable['u84'] = 'center';gv_vAlignTable['u50'] = 'center';document.getElementById('u239_img').tabIndex = 0;
HookClick('u239', false);

u239.style.cursor = 'pointer';
$axure.eventManager.click('u239', function(e) {

if (true) {

if (IsWidgetSelected('u239')) {
SetWidgetNotSelected('u239'); } else {
SetWidgetSelected('u239');
}
}
else
if ((GetCheckState('u241')) == (true)) {

SetCheckState('u241', false);

}
else
if (true) {

SetCheckState('u241', true);

}
});
HookClick('u260', false);
document.getElementById('u273_img').tabIndex = 0;
HookClick('u273', false);

u273.style.cursor = 'pointer';
$axure.eventManager.click('u273', function(e) {

if (true) {

	SetPanelVisibility('u262','hidden','none',500);

}
});
gv_vAlignTable['u76'] = 'center';document.getElementById('u271_img').tabIndex = 0;
HookClick('u271', false);

u271.style.cursor = 'pointer';
$axure.eventManager.click('u271', function(e) {

if (true) {

	SetPanelVisibility('u262','hidden','none',500);

}
});
gv_vAlignTable['u209'] = 'top';gv_vAlignTable['u94'] = 'center';gv_vAlignTable['u60'] = 'center';gv_vAlignTable['u9'] = 'center';gv_vAlignTable['u270'] = 'center';gv_vAlignTable['u234'] = 'top';gv_vAlignTable['u131'] = 'center';gv_vAlignTable['u242'] = 'top';gv_vAlignTable['u64'] = 'center';gv_vAlignTable['u70'] = 'center';gv_vAlignTable['u272'] = 'center';gv_vAlignTable['u24'] = 'center';gv_vAlignTable['u188'] = 'center';gv_vAlignTable['u230'] = 'center';document.getElementById('u204_img').tabIndex = 0;

u204.style.cursor = 'pointer';
$axure.eventManager.click('u204', function(e) {

if (true) {

	SetPanelVisibility('u144','','none',500);

}
});
gv_vAlignTable['u274'] = 'center';gv_vAlignTable['u261'] = 'center';gv_vAlignTable['u132'] = 'top';gv_vAlignTable['u129'] = 'top';gv_vAlignTable['u58'] = 'center';gv_vAlignTable['u173'] = 'center';gv_vAlignTable['u171'] = 'center';gv_vAlignTable['u232'] = 'center';gv_vAlignTable['u178'] = 'center';document.getElementById('u8_img').tabIndex = 0;

u8.style.cursor = 'pointer';
$axure.eventManager.click('u8', function(e) {

if (true) {

	SetPanelVisibility('u12','hidden','none',500);

}
});
gv_vAlignTable['u96'] = 'center';gv_vAlignTable['u146'] = 'center';gv_vAlignTable['u196'] = 'center';document.getElementById('u15_img').tabIndex = 0;

u15.style.cursor = 'pointer';
$axure.eventManager.click('u15', function(e) {

if (true) {

SetFocusedWidgetText('[[LVAR1 + \'q\']]');

}
});
gv_vAlignTable['u80'] = 'center';gv_vAlignTable['u1'] = 'center';gv_vAlignTable['u254'] = 'center';gv_vAlignTable['u148'] = 'center';document.getElementById('u145_img').tabIndex = 0;

u145.style.cursor = 'pointer';
$axure.eventManager.click('u145', function(e) {

if (true) {

	SetPanelVisibility('u144','hidden','none',500);

}
});

u237.style.cursor = 'pointer';
$axure.eventManager.click('u237', function(e) {

if (true) {

if (IsWidgetSelected('u235')) {
SetWidgetNotSelected('u235'); } else {
SetWidgetSelected('u235');
}
}
});
document.getElementById('u201_img').tabIndex = 0;

u201.style.cursor = 'pointer';
$axure.eventManager.click('u201', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Inbox.html');

}
});
document.getElementById('u199_img').tabIndex = 0;

u199.style.cursor = 'pointer';
$axure.eventManager.click('u199', function(e) {

if (true) {

	SetPanelVisibility('u262','toggle','none',500);

}
});
gv_vAlignTable['u215'] = 'center';gv_vAlignTable['u137'] = 'center';gv_vAlignTable['u244'] = 'center';gv_vAlignTable['u90'] = 'center';gv_vAlignTable['u18'] = 'center';gv_vAlignTable['u248'] = 'center';u224.tabIndex = 0;

u224.style.cursor = 'pointer';
$axure.eventManager.click('u224', function(e) {

if (true) {

	SetPanelVisibility('u220','','none',500);

	SetPanelState('u225', 'pd1u225','none','',500,'none','',500);
function waitufbae36f5f5da405f8cb203880569df481() {
}
setTimeout(waitufbae36f5f5da405f8cb203880569df481, 200);

}
});
gv_vAlignTable['u22'] = 'center';u143.tabIndex = 0;

u143.style.cursor = 'pointer';
$axure.eventManager.click('u143', function(e) {

if (true) {

	self.location.href='#';

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
gv_vAlignTable['u182'] = 'center';gv_vAlignTable['u180'] = 'center';gv_vAlignTable['u28'] = 'center';gv_vAlignTable['u194'] = 'center';