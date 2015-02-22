function StatusUpdate(el)
{
	var text = document.getElementById(el).value;
		
	var elDiv = el + "check";
	
	if(Valid(text,el))
	{
		document.getElementById(elDiv).innerHTML = "<img src = '../resources/images/png/ok.png'>";
		return true;
	}
	else
	{
		document.getElementById(elDiv).innerHTML = "<img src = '../resources/images/png/wrong.png'>";
		return false;
	}
}

function Valid(text,type)
{
	if(type=="mail")
		return CheckMail(text);
	if(type=="pass")
		return CheckPass(text);
	if(type=="login")
		return CheckLogin(text);
	if(type=="name")
		return CheckName(text);
	
	if(type=="servername")
		return CheckServerName(text);
	if(type=="fqdn")
		return Checkfqdn(text);
	if(type=="url")
		return CheckUrlPath(text);
	if(type=="port")
		return CheckPort(text);
	
	if(type=="checkInterval")
		return CheckCheckInterval(text);
	if(type=="refreshInterval")
		return CheckRefreshInterval(text);
	if(type=="serverTimeout")
		return CheckServerTimeout(text);
	if(type=="smtpAdress")
		return CheckSmtpAdress(text);
	if(type=="smtpPort")
		return CheckPort(text);

}

function CheckMail(value) {

	var reg = /^\w+([\.\-]?\w+)*@\w+([\.\-]?\w+)*(\.\w{2,6})+$/i;
	
	if ( value.length > 128 ) {
		return false; 
	}

	return value.match(reg); 
}

function CheckSmtpAdress(value) {

	var reg1 = /^\w+([\.\-]?\w+)*@\w+([\.\-]?\w+)*(\.\w{2,6})+$/i;
	var reg2 = /^.*<?\w+([\.\-]?\w+)*@\w+([\.\-]?\w+)*(\.\w{2,6})+>?$/i;
	
	if ( value.length > 128 ) {
		return false; 
	}

	return (value.match(reg1) || (value.match(reg2)));; 
}

function CheckLogin(value) {

	var reg = /^[a-zA-Z]{1,16}$/i;
	
	return value.match(reg);
}

function CheckPass(value) {

	var reg = /^.{6,16}$/;
	
	return value.match(reg);
}

function CheckName(value)
{
	var reg1 = /^[A-Z]{1}[a-z]*([a-z]*\-[a-z]+)* [A-Z]{1}[a-z]*([a-z]*\-[a-z]+)*$/;
	var reg2 = /^[А-Я]{1}[а-я]*([а-я]*\-[а-я]+)* [А-Я]{1}[а-я]*([а-я]*\-[а-я]+)*$/;
	
	if ( value.length > 128 ) {
		return false; 
	}

	return (value.match(reg1) || (value.match(reg2)));
}

function CheckPort(value) {
	var intRegex = /^\d+$/;
	if(value.match(intRegex)){
		return ((value>65535) || (value < 1))?false:true;
	}else{
		return false;
	}
}

function Checkfqdn(value)
{
	var reg1 = /((?=^.{1,128}$)(^(?:(?!\d+\.)[a-zA-Z0-9_\-]{1,63}\.?)+(?:[a-zA-Z]{2,})$))/;
	var reg2 = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
	
	if ( value.length > 128 ) {
		return false; 
	}
	
	return (value.match(reg1) || (value.match(reg2)));
}

function CheckServerName(value) {

	//var reg = /(^[a-zA-Z\-_0-9]+)(\.[a-zA-Z\-_0-9]+)*\.[a-z]{2,6}$/;
	var reg = /((?=^.{1,128}$)(^(?:(?!\d+\.)[a-zA-Z0-9_\-]{1,63}\.?)+(?:[a-zA-Z]{2,})$))/;

	if ( value.length > 128 ) {
		return false; 
	}	

	return value.match(reg);
}


function CheckUrlPath(value) {

	var reg = /^([//?].*)?$/;

	if ( value.length > 128 ) {
		return false; 
	}
	
	return value.match(reg);
}

function CheckCheckInterval(value) {
	var intRegex = /^\d+$/;
	if(value.match(intRegex)){
		return ((value>36000) || (value < 1))?false:true;
	}else{
		return false;
	}
}

function CheckRefreshInterval(value) {
	var intRegex = /^\d+$/;
	if(value.match(intRegex)){
		return ((value>36000) || (value < 1))?false:true;
	}else{
		return false;
	}
}

function CheckServerTimeout(value) {
	var intRegex = /^\d+$/;
	if(value.match(intRegex)){
		return ((value>300) || (value < 1))?false:true;
	}else{
		return false;
	}
}

function CheckNewUserSend()
{
	if(StatusUpdate('name')&&StatusUpdate('login')&&StatusUpdate('pass')&&StatusUpdate('mail'))
	{
		return true;
	}
	else
	{
		return false;
	}
}

function CheckNewServerSend()
{
	if(StatusUpdate('servername')&&StatusUpdate('fqdn')&&StatusUpdate('port')&&StatusUpdate('url'))
	{
		return true;
	}
	else
	{
		return false;
	}
}

function CheckSettingsSend()
{
	return 	StatusUpdate('checkInterval')&&StatusUpdate('refreshInterval')&&StatusUpdate('serverTimeout')&&StatusUpdate('smtpAdress')&&StatusUpdate('smtpPort');
}
