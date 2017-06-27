$(document).ready(function(e) {
    $(".custom_chk").on('click',function(){
	var prv_input = $(this).prev();
	if(prv_input && prv_input != undefined){
		if($(prv_input).is(':checked')){
			$(prv_input).prop('checked',false)
			}else{
				$(prv_input).prop('checked',true)
				}
		}
	})
});


function submitContactForm(){
	
	console.log("onclick");

var select_value = $(".bootstrap-select button.dropdown-toggle").attr("title");

	var empty = 0;
	var valid_form = true;
    $('input[data-req="true"]').each(function(){
		var input_id = $(this).attr("id");
       if (this.value == "") {
			$("#"+input_id).addClass("text-field-error");
			$("#error_msg_"+input_id).show();

			$("#"+input_id).keypress(function(){
				$("#"+input_id).removeClass("text-field-error"); 
				$("#error_msg_"+input_id).hide();
			});
			 valid_form = false;
       }
//	   if (select_value=="- Select -"){
//			var select_id = $("div.btn-group.bootstrap-select button.btn");
//				$(select_id).addClass("text-field-error");
//				$("#error_msg_requestType").show();
//				$(select_id).click(function(){
//					$(select_id).removeClass("text-field-error"); 
//					$("#error_msg_requestType").hide();
//				});
//				valid_form = false;
//		}
//		if (echeck($("#email").val())==false){
//				$("#email").addClass("text-field-error");
//				$("#error_msg_email").show();
//				$("#email").keypress(function(){
//					$("#email").removeClass("text-field-error"); 
//					$("#error_msg_email").hide();
//				});
//				valid_form = false;
//		}
//		/*if (echeck($("#emailConnect").val())==false){
//				$("#email").addClass("text-field-error");
//				$("#error_msg_email").show();
//				$("#email").keypress(function(){
//					$("#email").removeClass("text-field-error"); 
//					$("#error_msg_email").hide();
//				});
//				valid_form = false;
//		}*/
//		if (validatePhone($("#phoneNumber").val())==false){
//				$("#phoneNumber").addClass("text-field-error");
//				$("#error_msg_phoneNumber").show();
//				$("#phoneNumber").keypress(function(){
//					$("#phoneNumber").removeClass("text-field-error"); 
//					$("#error_msg_phoneNumber").hide();
//				});
//				valid_form = false;
//		}
		
    })
		return valid_form;
}


function echeck(str) 
{
	var at="@";
	var dot=".";
	var lat=str.indexOf(at);
	var lstr=str.length;
	var ldot=str.indexOf(dot);
	var domainstr = str.substring(lat+1,ldot);

	var invalidaddress=new Array(); // list of blocked emails
	invalidaddress[0]="hotmail";
	invalidaddress[1]="yahoo";
	invalidaddress[2]="rocketmail";

	for(i=0; i < invalidaddress.length; i++)
	{
		if ( domainstr == invalidaddress[i])
		{
			alert("Please use your corporate email address instead of web email addresses like hotmail or yahoo.");
			return false;
		}
	}

	if (str.indexOf(at)==-1){
	   $("#first_name").hide();
			$("#last_name").hide();
			$("#email_id").show();
			$("#company_name").hide();
			$("#phone_no").hide();
			$("#request_type").hide();
	   return false;
	}

	if (str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr){
	   $("#first_name").hide();
			$("#last_name").hide();
			$("#email_id").show();
			$("#company_name").hide();
			$("#phone_no").hide();
			$("#request_type").hide();
	   return false;
	}

	if (str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr){
		$("#first_name").hide();
			$("#last_name").hide();
			$("#email_id").show();
			$("#company_name").hide();
			$("#phone_no").hide();
			$("#request_type").hide();
		return false;
	}

	 if (str.indexOf(at,(lat+1))!=-1){
		$("#first_name").hide();
			$("#last_name").hide();
			$("#email_id").show();
			$("#company_name").hide();
			$("#phone_no").hide();
			$("#request_type").hide();
		return false;
	 }

	 if (str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot){
		$("#first_name").hide();
			$("#last_name").hide();
			$("#email_id").show();
			$("#company_name").hide();
			$("#phone_no").hide();
			$("#request_type").hide();
		return false;
	 }

	 if (str.indexOf(dot,(lat+2))==-1){
		$("#first_name").hide();
			$("#last_name").hide();
			$("#email_id").show();
			$("#company_name").hide();
			$("#phone_no").hide();
			$("#request_type").hide();
		return false;
	 }

	 if (str.indexOf(" ")!=-1){
		$("#first_name").hide();
			$("#last_name").hide();
			$("#email_id").show();
			$("#company_name").hide();
			$("#phone_no").hide();
			$("#request_type").hide();
		return false;
	 }	 
	 return true;					
}

function validatePhone(txtPhone) {
	var a = $("#phoneNumber").val();
	var filter = /^((\+[1-9]{1,4}[ \-]*)|(\([0-9]{2,3}\)[ \-]*)|([0-9]{2,4})[ \-]*)*?[0-9]{3,4}?[ \-]*[0-9]{3,4}?$/;
	if (filter.test(a)) {
	return true;
	}
	else {
		return false;
	}
}

function getParams()
{
	var form_type = '';
	var firstName = $("#firstName").val();
	var lastName = $("#lastName").val();
	var email = $("#email").val();
	var company = $("#companyName").val();
	var Phone = $("#phoneNumber").val();
	var requestType = $(".bootstrap-select button.dropdown-toggle").attr("title");
	var requestDetails = $("#requestDetails").val();
	var sourcePage = '';
	var collateral = '';


	params =  "form_type="+form_type+"&firstName="+firstName+"&lastName="+lastName+"&email="+email+"&company="+company+"&Phone="+Phone+"&requestDetails="+requestDetails+"&sourcePage="+sourcePage+"&Collateral="+collateral+"&requestType="+requestType;

	return params;
}

function isNumberKey(evt)
{
	var charCode = (evt.which) ? evt.which : event.keyCode
		if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;

		return true;
}

function refreshCaptcha() {
	$.post("/contact-form/captcha.php",{page: "page_index"},function(data, status){
	   if(status=='success'){
	   $("#captcha_label").html(data);
	  }
		});
}