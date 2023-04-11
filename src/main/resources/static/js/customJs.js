/**
* Afficher une boite de dialogue modale avec un callback appelé en cas de confirmation
*/
function showValidationDialog(message, callback){
	$('#dialog-button-action').unbind("click");
	openModal("Attention", message, true, "Valider", function() {
		callback.call();
		$('#dialog').modal('hide');
	}, true);
}

/**
* Mettre en place une validation lors des déplacements d'éléments dans une duallistbox
*/
function handleMoveValidation(duallistbox_id) {
	
	var moveButtonMap = new Map();
	moveButtonMap.set('move', 'bootstrap-duallistbox-nonselected-list_'+duallistbox_id);			
	moveButtonMap.set('remove', 'bootstrap-duallistbox-selected-list_'+duallistbox_id);
	
	var moveAllButtonMap = new Map();
	moveAllButtonMap.set('moveall', 'bootstrap-duallistbox-nonselected-list_'+duallistbox_id);
	moveAllButtonMap.set('removeall', 'bootstrap-duallistbox-selected-list_'+duallistbox_id);
	
	function validateOnMapElements(value, element, map) {
		var validationHandler = function(event) {
			// Empécher le déplacement car la boite de dialogue est ouverte en asynchrone
			event.stopImmediatePropagation();
			
			selection = $("#"+value+" option:selected");
			if (selection.length>0 ) {
				showValidationDialog("Voulez-vous déplacer la sélection ?", function(){
					$._data($('.'+element)[0]).events.click[1].handler.call();								
				});
			}
		}
		addValidationHandler(element, validationHandler);
	}
	
	function validateAllOnMapElements(value, element, map) {				  
		var validationHandler = function(event) {
			// Empécher le déplacement car la boite de dialogue est ouverte en asynchrone
			event.stopImmediatePropagation();
			
			options = $("#"+value+" option");
			if (options.length > 0) {
				showValidationDialog("Voulez-vous déplacer tous les élements ?", function(){
					$._data($('.'+element)[0]).events.click[1].handler.call();								
				});
			}
		}
		addValidationHandler(element, validationHandler);
	}
	
	moveButtonMap.forEach(validateOnMapElements);				
	moveAllButtonMap.forEach(validateAllOnMapElements);
}

/**
* Définir le handler de click principal sur un bouton de déplacement d'une duallistbox.
* Permet de récupérer l'évent en premier pour controler la propagation vers les autres handlers.
*/
function addValidationHandler(button, validationHandler) {
	$('.'+button).on('click', validationHandler)
	var clickHandlers = $._data($('.'+button)[0]).events.click;
	clickHandlers.reverse(); // Mettre le handler de validation en premier dans la liste
}
function clearFields() {
	document.getElementById("incident-objet").value=""
	document.getElementById("email").value=""
	document.getElementById("incident-description").value=""
	document.getElementById("incident-nature").value=""
	document.getElementById("incident-applications").value=""
	document.getElementById("incident-environnement").value=""
	document.getElementById("incident-impact").value=""
	document.getElementById("incident-debut").value=""
	document.getElementById("incident-fin").value=""
	document.getElementById("incident-info").value=""

}
function confirmDelete(id,name,url){
	openModal("Attention", "Etes vous sur de vouloir supprimer : "+name, true, "Valider", function(){ location.replace(url);}, true);	
}


//Une fonction qui se déclenche en tapant sur les input de la page création d'incident
function onIncidentInputChange(textInputId , counterId){
     let textInput = document.getElementById(textInputId);
     let counter = document.getElementById(counterId);
     textInput.addEventListener('keyup',(e)=>{
            if(window.location.pathname.includes("incident-follow")){
                let followCounter = document.getElementById(counterId+"-follow-counter")
                if(followCounter){
                        followCounter.style.display="none";
                }

            }
     		counter.innerHTML= textInput.value.length+"/"+counter.innerHTML.split("/")[1]
     		localStorage.setItem(counterId,textInput.value.length)
     	})


    }

 //Méthode de validation du champ input de destinataire en création d'incident

function validateEmailList(){

	        let incidentEmail = document.querySelector(".incident-input")
            let incidentDestinationDiv = document.querySelector(".incidentDestination-div")
            let incidentCreationPrelook = document.querySelector(".incident-creation-prelook");
            let incidentEmailError = document.querySelector(".incidentEmailError");
            const reguarExpMail = /^([_a-zA-Z0-9-.]+)@([_a-zA-Z0-9-.]+).([a-zA-Z]{2,5})$/;

            const reguarExpGroup = /^@([_a-zA-Z0-9-])/;
            let isError = false;
            let list = incidentEmail.value;
            let diff = list.split(" ");
            if (list.length > 0 ) {

                isError = diff.find(
                (element) =>
                    !reguarExpMail.test(element.trim()) &&
                    !reguarExpGroup.test(element.trim())
                )
                ? true
                : false;
            } else {
                isError = false

            }
            if (isError) {
                incidentCreationPrelook.setAttribute("disabled", true);
                incidentDestinationDiv.style.marginBottom = "0.3vh"
                incidentEmailError.style.display = "inline";
                incidentEmailError.innerHTML = diff.length > 1 ? "Une adresse mail n'est pas au bon format ." : "Les destinataires doivent être séparés par un espace ."
            } else {
                incidentCreationPrelook.removeAttribute("disabled");
                 incidentDestinationDiv.style.marginBottom = "2vh"
                incidentEmailError.style.display = "none";
            }

}

function openModal(title, content, displayButtonAction, textButtonAction, action, displayButtonCancel) {
	$('#dialog-title').text(title);
	$('#dialog-content').html(content);
	$('#dialog-button-action-edit').hide();
	
	if (displayButtonCancel) {
		$('#dialog-button-cancel').show();
	}else{
		$('#dialog-button-cancel').hide();
	}
	
	if (displayButtonAction) {
		$('#dialog-button-action').val(textButtonAction);
		$('#dialog-button-action').text(textButtonAction);
		$('#dialog-button-action').show();
		$('#dialog-button-action').click(action);
	} else {
		$('#dialog-button-action').hide();
	}
	$('#dialog').modal('show');
}

function activeNavItemBar() {
	$(document).ready(function () {
        // Let's get rid of the query string
        var path = location.pathname.split('?')[0];
        var activeLink = path.split('/')[1]        
        if(activeLink=="/"){$("nav li").removeClass("active");}else{
        var parent = $('a[href*="' + activeLink + '"]').parent('li');
        // These two lines check if you have dropdowns in your nav-bar
//        if (parent.closest('ul').hasClass('dropdown-menu')) {
//            parent.parents('.dropdown').addClass('active');
//        }
        parent.addClass('active');
        }
    });	
}
activeNavItemBar();


function dynmicfields(){	    
    /////////MAIL
	var max_fields_limit_Mail      = 10; //set limit for maximum input fields
    var xMail = 1; //initialize counter for text box  
    
    $('#add_more_buttonMail').click(function(e){
    	var count = $('#sizeHoursWhenMailIsAllowed').val();
        //click event on add more fields button having class add_more_button
        e.preventDefault();
        if(xMail < max_fields_limit_Mail){ //check conditions
        	xMail++; //counter increment 
            $('.input_fieldsMail_container_part').last().after().append('<div class="row ">'
            		+'<select class="form-group" id="hoursWhenMailIsAllowed'+count+'.day" name="hoursWhenMailIsAllowed['+count+'].day">'
            		+'<option value="MONDAY">lundi</option><option value="TUESDAY">mardi</option><option value="WEDNESDAY">mercredi</option><option value="THURSDAY">jeudi</option>'
            		+'<option value="FRIDAY">vendredi</option><option value="SATURDAY">samedi</option><option value="SUNDAY">dimanche</option>'
            		+'</select>'
            		+'<input type="hidden" value="00:00" id="hoursWhenMailIsAllowed'+count+'.minHour" name="hoursWhenMailIsAllowed['+count+'].minHour">'
            		+'<input type="hidden" value="23:59" id="hoursWhenMailIsAllowed'+count+'.maxHour" name="hoursWhenMailIsAllowed['+count+'].maxHour">'
            		+'<div class="form-group  col-md-8"><span id="time-range">'
            		+'De <span id="slider-timeMail'+count+'">00:00</span> a <span id="slider-timeMail'+count+'a">23:59</span>'
            		+'<span class="sliders_step1">'
            		+'<div id="slider-rangeMail'+count+'" class="ui-slider ui-corner-all ui-slider-horizontal ui-widget ui-widget-content">'
            		+'<div class="ui-slider-range ui-corner-all ui-widget-header" style=""></div>'
            		+'<span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default" style="left: 0%;"></span>'
            		+'<span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default" style="left: 100%;"></span>'
            		+'</div></span></span></div>'
            		+'<a href="#" class="remove_field" style="margin-left:10px;"><i class="color-blue fas fa-times fa-s"></i></a></div>'
            		+''
            		+'</div>');
            sliderHours(count,"Mail");
            $('#sizeHoursWhenMailIsAllowed').val(++count);         
        }
    });
    
    $('.input_fieldsMail_container_part').on("click",".remove_field", function(e){ //user click on remove text links
        e.preventDefault(); $(this).parent('div').remove(); xMail--;
    })    
    /////////////////////////////////////////
    
    //SMS
    var max_fields_limit_Sms      = 10; //set limit for maximum input fields
    var xSms = 1; //initialize counter for text box  
    
    $('#add_more_buttonSms').click(function(e){
    	var countSms = $('#sizeHoursWhenSmsIsAllowed').val();
    	e.preventDefault();
    	if(xSms < max_fields_limit_Sms){
    		xSms++;
    		$('.input_fieldsSms_container_part').last().after().append('<div class="row ml-1">'
    				+'<select class="form-group" id="hoursWhenSmsIsAllowed'+countSms+'.day" name="hoursWhenSmsIsAllowed['+countSms+'].day">'
            		+'<option value="MONDAY">lundi</option><option value="TUESDAY">mardi</option><option value="WEDNESDAY">mercredi</option><option value="THURSDAY">jeudi</option>'
            		+'<option value="FRIDAY">vendredi</option><option value="SATURDAY">samedi</option><option value="SUNDAY">dimanche</option>'
            		+'</select>'
            		+'<input value="00:00" type="hidden" id="hoursWhenSmsIsAllowed'+countSms+'.minHour" name="hoursWhenSmsIsAllowed['+countSms+'].minHour">'
            		+'<input value="23:59" type="hidden" id="hoursWhenSmsIsAllowed'+countSms+'.maxHour" name="hoursWhenSmsIsAllowed['+countSms+'].maxHour">'
            		+'<div class="form-group  col-md-8"><span id="time-range">'
            		+'De <span id="slider-timeSms'+countSms+'">00:00</span> a <span id="slider-timeSms'+countSms+'a">23:59</span>'
            		+'<span class="sliders_step1">'
            		+'<div id="slider-rangeSms'+countSms+'" class="ui-slider ui-corner-all ui-slider-horizontal ui-widget ui-widget-content">'
            		+'<div class="ui-slider-range ui-corner-all ui-widget-header" style=""></div>'
            		+'<span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default" style="left: 0%;"></span>'
            		+'<span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default" style="left: 100%;"></span>'
            		+'</div></span></span></div>'
            		+'<a href="#" class="remove_field" style="margin-left:10px;"><i class="color-blue fas fa-times fa-s"></i></a></div>'
            		+''
            		+'</div>');
    		sliderHours(countSms,'Sms');
            $('#sizeHoursWhenSmsIsAllowed').val(++countSms);    		
    	}   	
    	
    });
    
    $('.input_fieldsSms_container_part').on("click",".remove_field", function(e){ //user click on remove text links
        e.preventDefault(); $(this).parent('div').remove(); xSms--;
    })    
    /////////////////////////////////////////////////////////////
    
    //ASTREINTE
    var max_fields_limit_Astreinte      = 10; //set limit for maximum input fields
    var xAstreinte = 1; //initialize counter for text box  
    
    $('#add_more_buttonAstreinte').click(function(e){
    	var countAstreinte = $('#sizeHoursWhenAstreinteIsAllowed').val();
    	e.preventDefault();
    	if(xAstreinte < max_fields_limit_Astreinte){
    		xAstreinte++;
    		$('.input_fieldsAstreinte_container_part').last().after().append('<div class="row ml-1">'
    				+'<select class="form-group" id="hoursWhenAstreinteIsAllowed'+countAstreinte+'.day" name="hoursWhenAstreinteIsAllowed['+countAstreinte+'].day">'
            		+'<option value="MONDAY">lundi</option><option value="TUESDAY">mardi</option><option value="WEDNESDAY">mercredi</option><option value="THURSDAY">jeudi</option>'
            		+'<option value="FRIDAY">vendredi</option><option value="SATURDAY">samedi</option><option value="SUNDAY">dimanche</option>'
            		+'</select>'
            		+'<input value="00:00" type="hidden" id="hoursWhenAstreinteIsAllowed'+countAstreinte+'.minHour" name="hoursWhenAstreinteIsAllowed['+countAstreinte+'].minHour">'
            		+'<input value="23:59" type="hidden" id="hoursWhenAstreinteIsAllowed'+countAstreinte+'.maxHour" name="hoursWhenAstreinteIsAllowed['+countAstreinte+'].maxHour">'
            		+'<div class="form-group  col-md-8"><span id="time-range">'
            		+'De <span id="slider-timeAstreinte'+countAstreinte+'">00:00</span> a <span id="slider-timeAstreinte'+countAstreinte+'a">23:59</span>'
            		+'<span class="sliders_step1">'
            		+'<div id="slider-rangeAstreinte'+countAstreinte+'" class="ui-slider ui-corner-all ui-slider-horizontal ui-widget ui-widget-content">'
            		+'<div class="ui-slider-range ui-corner-all ui-widget-header" style=""></div>'
            		+'<span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default" style="left: 0%;"></span>'
            		+'<span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default" style="left: 100%;"></span>'
            		+'</div></span></span></div>'
            		+'<a href="#" class="remove_field" style="margin-left:10px;"><i class="color-blue fas fa-times fa-s"></i></a></div>'
            		+''
            		+'</div>');
    		sliderHours(countAstreinte,'Astreinte');
            $('#sizeHoursWhenAstreinteIsAllowed').val(++countAstreinte);    		
    	}   	
    	
    });
    
    $('.input_fieldsAstreinte_container_part').on("click",".remove_field", function(e){ //user click on remove text links
        e.preventDefault(); $(this).parent('div').remove(); xSms--;
    })    
    /////////////////////////////////////////////////////////////    
}

function convertHhMmToMinutes(s){
	var a = s.split(':');
	var minutes = (+a[0]) * 60 + (+a[1]) ;
	return minutes;
}

function sliderHours(index, field){	
	var start = $("input[id='hoursWhen"+field+"IsAllowed"+index+".minHour']").val();	
	var startInMinutes = convertHhMmToMinutes(start);
	
	var end = $("input[id='hoursWhen"+field+"IsAllowed"+index+".maxHour']").val();	
	var endInMinutes = convertHhMmToMinutes(end);
	
	var v = [startInMinutes, endInMinutes];
	$("#slider-range"+field+index).slider({
	    range: true,
	    min: 0,
	    max: 1440,
	    step: 15,
	    values: v,
	    slide: function (e, ui) {
	        var hours1 = Math.floor(ui.values[0] / 60);
	        var minutes1 = ui.values[0] - (hours1 * 60);

	        if (hours1.toString().length == 1){ hours1 = '0' + hours1; }
	        if (minutes1.toString().length == 1) minutes1 = '0' + minutes1;
	        if (minutes1 == 0) minutes1 = '00';
	       

	        $('#slider-time'+field+index).html(hours1 + ':' + minutes1);
	        $("input[id='hoursWhen"+field+"IsAllowed"+index+".minHour']").val(hours1 + ':' + minutes1)

	        var hours2 = Math.floor(ui.values[1] / 60);
	        var minutes2 = ui.values[1] - (hours2 * 60);

	        if (hours2.toString().length == 1) hours2 = '0' + hours2;
	        if (minutes2.toString().length == 1) minutes2 = '0' + minutes2;
	        if (minutes2 == 0) minutes2 = '00';       
	        if (hours2 == 24 && minutes2 == 0) {          
	          hours2 = '23'
	          minutes2 = '59';
	        }
	        $('#slider-time'+field+index+'a').html(hours2 + ':' + minutes2);
	        $("input[id='hoursWhen"+field+"IsAllowed"+index+".maxHour']").val(hours2 + ':' + minutes2)
	    }
	});
}

/* Sample function that returns boolean in case the browser is Internet Explorer*/
function isIE() {
  ua = navigator.userAgent;
  /* MSIE used to detect old browsers and Trident used to newer ones*/
  var is_ie = ua.indexOf("MSIE ") > -1 || ua.indexOf("Trident/") > -1;
  
  return is_ie; 
}

var dataTableConfig = {
	"paging": true,
	"lengthMenu": [ [-1, 10, 25, 50, 100], ["Tout", 10, 25, 50, 100] ],
	"responsive": true,
	"order": [],
    "columnDefs": [ {
      "targets"  : 'no-sort',
      "orderable": false,
    }],
    "language": {
        "search": "Rechercher: _INPUT_",
        "searchPlaceholder": "Critère...",
        "lengthMenu": "Afficher _MENU_ lignes",
        "info": "Affiche les lignes de _START_ a _END_ sur un total de _TOTAL_",
        "zeroRecords":    "Aucune donnée correspondante trouvée",
	      "paginate": {
	          "first":      "Début",
	          "last":       "Fin",
	          "next":       "Suivant",
	          "previous":   "Précédent"
	      }
      },
};

<!-- Initialize all tooltips -->
$(function () {
  $('[data-toggle="tooltip"]').tooltip({
	    delay: {
	        show: 300, //Delay tooltip
	        hide: 0
	    }
	})
});

<!-- Fixed table header does not work with_ Internet Explorer -->
if(!isIE()) {
	<!-- Initialize fix table headers -->
	$(document).ready(function() {
		$('.table-fix-header').floatThead({
	    	top: 60,
	    	responsiveContainer: function($table){
	            return $table.closest(".table-responsive");
	        }
	    });
	});
}

/*S'exécute en entrant du texte de l'alerte sur la page bandeauAlerte/creation */

function onInputTexteAlerte(){
          let bdaCreation=document.querySelector('.bdaCreation')
          let button= document.getElementById('submit-alerte')
          if (bdaCreation.value.trim()!==''){
                button.removeAttribute('disabled')
          }
          else{
                button.setAttribute('disabled' , 'true')
          }

          bdaCreation.addEventListener('keyup',(e)=>{
                counter.innerHTML=bdaCreation.value.length
          })

}
function loadLastMiseAjourDate(url, callback) {
  var xhr = new XMLHttpRequest();

  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4) {
      callback(xhr.response);
    }
  }

  xhr.open('GET', url, true);
  xhr.send('');
}


window.addEventListener('load',()=> {
           let toast= document.querySelector(".toast")
           toast.style.display="none"


          if (window.location.pathname==="/information/status"){
                    let tools = document.querySelectorAll(".tool");



                    tools.forEach((element) =>
                      element.addEventListener("click", () => {
                        let tickets = element.parentNode.nextSibling.nextSibling;
                        if (tickets.classList.contains("hideticket")) {
                          tickets.classList.remove("hideticket");
                        } else {
                          tickets.classList.add("hideticket");
                        }
                      })
                    );
                    let domain=new URLSearchParams(window.location.search).get("domain")
                    affichageBandeau()
                    let informationDateText=document.getElementById("information-date-text")
                    informationDateText.innerHTML="Dernière mise à jour : "+displayDate()


                    setInterval(()=>{
                     location.reload();
                     },60000)


          }

          if (cloture && localStorage.getItem("cltr")==="yes"){
                     let options = {
                          animation:true,
                          delay : 2500
                      }
                     toast.style.removeProperty('display');

                      let clotureToast= new bootstrap.Toast(toast,options)
                      clotureToast.show()
                      localStorage.removeItem("cltr")

                    }
         })

function displayDate(){
      let date=new Date()
      let days = formatInt(date.getDate())
      let month = formatInt(date.getMonth()+1)
      let year = date.getFullYear()
      let hours = formatInt(date.getHours())
      let minutes = formatInt(date.getMinutes())
      let dateToDisplay = `${days}/${month}/${year} ${hours}:${minutes}`
      return dateToDisplay
}

function formatInt(number){
    if(number<10){
      return `0${number}`
    }
    else return `${number}`
}




function affichageBandeau(){
    let texteAlerte=document.querySelector('.texteAlerte')
    let tickerWrap=document.querySelector('.ticker-wrap ')

   if(texteAlerte){
         let length=texteAlerte.innerHTML.length;
          switch (true) {
                case (length===1) :
                    texteAlerte.classList.add("ticker-level0")
                break;
                case (length<4 && length>=2) :
                    texteAlerte.classList.add("ticker-level1")
                break;

                case (length<=7 && length>=4) :
                     texteAlerte.classList.add("ticker-level2")
                break;
                case (length<=10 && length>=8) :
                      texteAlerte.classList.add("ticker-level3")
                break;
               case (length<=16 && length>=11) :
                      texteAlerte.classList.add("ticker-level8")
                break;

                case (length<=23 && length>=17) :
                      texteAlerte.classList.add("ticker-level4")
                break;
                case (length<30 && length>=24) :
                      texteAlerte.classList.add("ticker-level5")
                break;
                case (length>=30 && length<=35) :
                       texteAlerte.classList.add("ticker-level6")
                break;
                  case (length>=36 && length<=80) :
                       texteAlerte.classList.add("ticker-level7")
                break;
                default:
                       texteAlerte.classList.add("ticker-level9")
                }
    }
   }

    function finClotureAction(){
        var oReq = new XMLHttpRequest();
        oReq.open("GET", '/clotureSuccess', true);
        oReq.send();
        localStorage.setItem("cltr","yes");
        if (window.location.pathname==="/bdalerte/creationFormSubmit" || window.location.pathname==="/bdalerte/miseAjourFormSubmit" ){
                        window.location.replace("/bdalerte/creation");

                  }

        else{
        location.reload(true);
        }
    }
    function setTicketDate(input){
    return "test function"
    }

    let redChart = document.getElementById("red-chart")
    let yellowChart = document.getElementById("yellow-chart")
    let greenChart = document.getElementById("green-chart")
    let radio=document.getElementById("green-chart-radio-button")





    let labelsTexts= ["Déclaration d'incident" ,"En cours de traitement", "Clôture d'incident"]

    let chartButtonsList=[redChart,yellowChart,greenChart]




    if(redChart && yellowChart && greenChart && radio){
             if ((window.location.pathname.includes("incident-creation")  || window.location.pathname.includes("incident-follow") )&& localStorage.getItem('graphicalCharterMessage')){

                    let counterIds= Object.keys(localStorage).filter(e => e!=='graphicalCharterMessage')

                    if(!window.location.pathname.includes("incident-follow")){
                          counterIds.map(id => {
                                            let counterElement = document.getElementById(id);
                                            counterElement.innerHTML= localStorage.getItem(id)+"/"+counterElement.innerHTML.split("/")[1]

                                        })
                    }


                    let index = labelsTexts.findIndex((el)=>el==localStorage.getItem('graphicalCharterMessage'));
                    if(index!== -1){
                       chartButtonsList[index].style.opacity="1"
                       chartButtonsList[index].innerHTML = localStorage.getItem('graphicalCharterMessage')
                       localStorage.clear();
                    }
                }

        chartButtonsList.forEach((element,index) => {

             element.addEventListener("click",()=>{

             if(window.location.pathname.includes("incident-follow") && element==redChart ){
                    return
             }

             let otherCharts=chartButtonsList.filter(word => word !== element);
             element.style.opacity="1"
             element.innerHTML = labelsTexts[index]
             otherCharts.forEach(el=>{
                if(el.style.opacity==="1"){
                    el.style.opacity=0.5
                    el.innerHTML=""
                }

             })
        }
        )})

    }

function aideClickHandler(){

   var iframe="<embed src='../pdf/FlashInfo.pdf' width='1300' height='700'>";
   let dialogContent = document.getElementById("dialog-content") ;
   dialogContent.classList.add("text-center");
    $('.modal-dialog').css("maxWidth", "1400px")
    $('.modal-dialog').css("height", "80vw")
    openModal(
   			"Aide",
   			iframe,
   			false,
   			null,
   			function () {},
   			true
   			);
}




window.onbeforeunload = function(){
         if(!window.location.pathname.includes("incidentPrelook")){
               localStorage.removeItem('graphicalCharterMessage');
         }
};

$(document).ready(function () {
	$('.summernote').summernote({
		toolbar: [
			['fontstyle', ['fontname', 'fontsize', 'color', 'bold', 'italic', 'underline', 'deleteallformat']],

			['para', ['style', 'ul', 'ol', 'paragraph', 'height']],

		],
		callbacks: {
			onImageUpload: function (data) {
				data.pop();
			}
		}
	});

});