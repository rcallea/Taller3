package co.edu.uniandes.taller3.client;

import com.google.gwt.i18n.client.Constants;

public interface RSConstants extends Constants{
	
	/**
	 * á \u00E1		é \u00E9		í \u00ED		ó \u00F3		ú \u00FA		Ñ \u00F1		¿ \u00BF
	 * @return
	 */
	
	@DefaultStringValue("<h3>Recomendador Basado en contenido con Lucene</h3>")
	String cblTitle();

	@DefaultStringValue("Tama\u00F1o del dataset de pruebas")
	String cblDatasetSize();

	@DefaultStringValue("Tiempo de espera m\u00E1ximo en minutos")
	String cblWaitTime();

	@DefaultStringValue("M\u00EDnimo de frecuencia de t\u00E9rminos")
	String cblMinTermFrequency();

	@DefaultStringValue("M\u00EDnimo de frecuencia de documentos")
	String cblMinDocFrequency();

	@DefaultStringValue("Tama\u00F1o m\u00EDnimo de las palabras")
	String cblMinWordLen();

	@DefaultStringValue("Usuario")
	String cblUser();

	@DefaultStringValue("Precision")
	String cblPrecision();

	@DefaultStringValue("Recall")
	String cblRecall();

	@DefaultStringValue("Lista de items recomendados")
	String cblResultList();

	@DefaultStringValue("Calcular CBL")
	String cblSend();

	@DefaultStringValue("Calcular CBL2")
	String cblSend2();

	@DefaultStringArrayValue({"0;Cualquiera","1;1","2;2","3;3","4;4","5;5","6;6","7;7","8;8","9;9","10;10","15;15","20;20","25;25","30;30","35;35","40;40","45;45","50;50","55;55","60;60","65;65","70;70","75;75","80;80","85;85","90;90","95;95","100;100"})
	String[] cblMinTermFrequencyValues();

	@DefaultStringArrayValue({"0;Cualquiera","1;1","2;2","3;3","4;4","5;5","6;6","7;7","8;8","9;9","10;10","15;15","20;20","25;25","30;30","35;35","40;40","45;45","50;50","55;55","60;60","65;65","70;70","75;75","80;80","85;85","90;90","95;95","100;100"})
	String[] cblMinDocFrequencyValues();

	@DefaultStringArrayValue({"0;Cualquiera","1;1","2;2","3;3","4;4","5;5","6;6","7;7","8;8","9;9","10;10","11;11","12;12","13;13","14;14","15;15","16;16","17;17","18;18","19;19","20;20"})
	String[] cblMinWordLenValues();

	//Generales
	@DefaultStringValue("Ingrese una ciudad: ")
	String labelCity();
	
	@DefaultStringValue("Ingrese una categoria: ")
	String labelCategory();
	
	
	//View Filtrado colaborativo
	@DefaultStringValue("<h3>Recomendador por filtraje colaborativo</h3>")
	String cfTitle();

	@DefaultStringValue("Fecha de inicio y fin de la ventana de tiempo")
	String cfWindowDate();

	@DefaultStringValue("N\u00FAmero de vecinos")
	String cfNeighbors();

	@DefaultStringValue("N\u00FAmero de calificaciones a utilizar")
	String cfGradeNumber();

	@DefaultStringValue("Tipo de medida")
	String cfMeasureType();

	@DefaultStringValue("Tipo de recomendador")
	String cfRecommenderType();

	@DefaultStringValue("Usuario")
	String cfUser();

	@DefaultStringValue("Precision")
	String cfPrecision();

	@DefaultStringValue("Recall")
	String cfRecall();

	@DefaultStringValue("Lista de items recomendados")
	String cfResultList();

	@DefaultStringValue("Calcular CF")
	String cfSend();

	@DefaultStringArrayValue({"60 %", "70 %", "80 %"})
	String[] cfDatasetSizeValues();

	@DefaultIntValue(1)
	int cfNeighborsMinValue();

	@DefaultIntValue(250)
	int cfNeighborsMaxValue();

	@DefaultStringArrayValue({"false;Correlaci\u00F3n de Pearson", "true;Distancia coseno"})
	String[] cfMeasureTypeValues();

	@DefaultStringArrayValue({"false;Usuario-Usuario", "true;Item-Item"})
	String[] cfRecommenderTypeValues();

	@DefaultStringValue("Error en el campo")
	String uiFieldError();

	@DefaultStringValue("El campo se encuentra vac\u00EDo o tiene caracteres inv\u00E1lidos")
	String uiFieldErrorMessage();

	@DefaultStringValue("El campo se encuentra fuera del rango de valores v\u00E1lidos")
	String uiFieldOutOfBoundsMessage();
	
	// View Recomendador basado en contenido
	@DefaultStringValue("Recomendador basado en conocimiento")
	String contTitle();

	@DefaultStringValue("Ingrese una descripci\u00F3n del sitio que busca y lo que espera encontrar en este: ")
	String labelDescriptionBusiness();
	
	@DefaultStringValue("Recomendar")
	String contentSend();
	
	// View Recomendador hibrido
	@DefaultStringValue("Recomendar Hibrido")
	String hybridSend();
	
	@DefaultStringArrayValue({"1023 E Frye Rd",
		"Ahwatukee","Allegheny","Allentown","Anjou","Anthem",
		"Apache Junction","Arcadia",
		"Arlington","Aspinwall",
		"Avondale",	"Baden-Baden",
		"Baie-D'urfe","Balerno",
		"Banksville","Bapchule",
		"Beaconsfield",	"Bellevue",
		"Bellvue",	"Belmont","Bethel Park",
		"Bietigheim","Black Canyon City",
		"Blainville","Bloomfield",
		"Bocholt","Boisbriand",	"Bonnyrigg",
		"Bonnyrigg and Lasswade","Boucherville",
		"Boulder City",	"Braddock",	"Brentwood",
		"Bridgeville",	"Brookline","Brossard",
		"Bruchsal",	"Buckeye","C Las Vegas",
		"Cambridge","Carefree",	"Carnegie",
		"Casa Grande",	"Castle Shannon",
		"Cave Creek",	"Centennial Hills",
		"Central City Village",	"Central Henderson",
		"Centropolis Laval",	"Champaign",
		"Chandler",	"Chandler-Gilbert",
		"Charlotte","Charlotte (University)",
		"Chateau",	"Chomedey",
		"City of Edinburgh","Clark County",
		"Clover",	"Columbus",
		"Communauté-Urbaine-de-Montréal",
		"Concord",	"Concord Mills",
		"Conestogo",	"Coolidge",
		"Cote-des-Neiges-Notre-Dame-de-Grace",
		"Cote-Saint-Luc","Cottage Grove",
		"Crafton",	"Cramond",
		"Cramond Bridge","Cultural District",
		"Dalgety Bay",	"Dalkeith",
		"Dane",	"De Forest",
		"DeForest",	"Delmont",
		"Desert Ridge",	"Deux-Montagnes",
		"Deux-Montagnes Regional County Municipality",
		"Dollard-Des Ormeaux","Dollard-Des-Ormeaux",
		"Dormont",	"Dorval",
		"Downtown",	"Dravosburg",
		"Durmersheim",	"Eagan",
		"Edgewood",	"Edinburgh",
		"Edinburgh City of","Eggenstein-Leopoldshafen",
		"El Mirage","Enterprise",
		"Etna",	"Ettlingen",
		"Fabreville","Firth of Forth",
		"Fitchburg","Florence",
		"Forchheim","Fort Kinnaird",
		"Fort Mcdowell","Fort Mill",
		"Fountain Hills","Fountain Hls",
		"Ft. Mill","Gila Bend",
		"Gilbert","Glendale",
		"Glendale Az","Gold Canyon",
		"Goldfield","Goodyear",
		"Green Tree","Green Valley",
		"Greenfield Park",	"Guadalupe",
		"Hagenbach","Harrisburg",
		"Heidelberg","Henderson",
		"Henderson (Green  Valley)","Henderson (Stephanie)",
		"Henderson and Las vegas","Henderson, NV 89074",
		"Henderston","Higley",
		"Homestead","Huntersville",
		"Île des Soeurs","Indian Land",
		"Indian Trail",	"Ingram",
		"Inverkeithing","Jockgrim",
		"Juniper Green", "Karlsbad",
		"Karlsruhe","Kirkland",
		"Kitchener","La Prairie",
		"Lachine", "Lake Las Vegas",
		"Lake Wylie","LAS VEGAS",
		"Las Vegas East","Lasalle",
		"Lasswade",	"Last Vegas",
		"Laval","Laveen",
		"Lawrenceville","Le Sud-Ouest",
		"Le Vieux-Port","Leith",
		"L'Île-Bizard",	"L'Île-des-Soeurs",
		"Litchfield Park","Loanhead",
		"London","Longueuil",
		"Los Angeles",	"Lower Lawrenceville",
		"Luke AFB",	"Madison",
		"Maricopa",	"Maricopa County",
		"Mascouche","Mattews",
		"Matthews",	"Mc Farland",
		"Mc Kees Rocks","Mcfarland",
		"Mckees Rocks",	"McKeesport",
		"Mesa",	"Metro Phoenix",
		"Middleton", "Midlothian",
		"Millvale",	"Mint Hill",
		"Mirabel",	"Monona",
		"Monroe","Montéal",
		"Montreal",	"Montréal",
		"Montreal-Est",	"Montreal-Nord",
		"Montréal-Nord","Montreal-Ouest",
		"Montréal-Ouest","Montreal-West",
		"Mont-Royal",	"Morristown",
		"Mount Holly",
		"Mount Lebanon",
		"Mount Royal",
		"Mount Washington",
		"Mt Lebanon",
		"Mt. Oliver Boro",
		"Munhall",
		"Musselburgh",
		"N E Las Vegas",
		"N Las Vegas",
		"N W Las Vegas",
		"N. Las Vegas",
		"Nboulder City",
		"Nellis AFB",
		"Nellis Air Force Base",
		"Neuburg",
		"New Dundee",
		"New River",
		"New Town",
		"Newberry Springs",
		"Newbridge",
		"Newington",
		"North Las Vegas",
		"North Queensferry",
		"North Scottsdale",
		"Oakland",
		"Old Montreal",
		"Old Town",
		"Outremont",
		"Paradise",
		"Paradise Valley",
		"Penicuik",
		"Peoria",
		"Pfinztal",
		"Pheonix",
		"Phoenix",
		"Phoenix Sky Harbor Center",
		"Phoenix-Ahwatukee",
		"Pierrefonds",
		"Pineville",
		"Pittsburg",
		"Pittsburgh",
		"Pittsburgh/S. Hills Galleria",
		"Pittsburgh/Waterfront",
		"Pittsburrgh",
		"Pointe Claire",
		"Pointe-Aux-Trembles",
		"Pointe-Claire",
		"Portobello",
		"Quebec",
		"Queen Creek",
		"Queensferry",
		"Rankin",
		"Ratho",
		"Regent Square",
		"Rheinstetten",
		"Rio Verde",
		"Robinson Township",
		"Rock Hill",
		"Roosevelt",
		"Rosemere",
		"Rosemère",
		"Roslin",
		"Roxboro",
		"Saguaro Lake",
		"Saint Jacobs",
		"Saint Laurent",
		"Sainte-Ann-De-Bellevue",
		"Sainte-Anne-de-Bellevue",
		"Sainte-Genevieve",
		"Sainte-Therese",
		"Sainte-Thérèse",
		"Saint-Eustache",
		"Saint-Hubert",
		"Saint-Lambert",
		"Saint-laurent",
		"Saint-Leonard",
		"San Diego",
		"San Tan Valley",
		"Savoy",
		"Scotland",
		"Scottdale",
		"Scottsdale",
		"Scottsdale Country Acres",
		"Seattle",
		"Sedona",
		"Shadyside",
		"Sharpsburg",
		"South Gyle",
		"South Hills",
		"South Las Vegas",
		"South Queensferry",
		"Southside Flats",
		"Spring Valley",
		"Squirrel Hill",
		"St Clements",
		"St Jacobs",
		"St. Jacobs",
		"Stallings",
		"Stanfield",
		"Ste-Rose",
		"St-Laurent",
		"Stockbridge",
		"Stoughton",
		"Stowe",
		"Stowe Township",
		"Straiton",
		"Stutensee",
		"Stutensee neuthard",
		"Summerlin",
		"Summerlin South",
		"Sun City",
		"Sun City Anthem",
		"Sun City West",
		"Sun Lakes",
		"Sun Prairie",
		"Sunrise",
		"Surprise",
		"Surprise Crossing",
		"Swissvale",
		"Tega Cay",
		"Tempe",
		"Terrebonne",
		"Tolleson",
		"Tonopah",
		"Tonto Basin",
		"Tortilla Flat",
		"Tucson",
		"University",
		"Upper Saint Clair",
		"Urbana",
		"Verdun",
		"Verona",
		"Victoria Park",
		"Vimont",
		"W Henderson",
		"W Spring Valley",
		"W Summerlin",
		"Waddell",
		"Waldbronn",
		"Water of Leith",
		"Waterloo",
		"Waunakee",
		"Waxhaw",
		"Weddington",
		"Weingarten",
		"Weingarten (Baden)",
		"Wesley Chapel",
		"West Homestead",
		"West Mifflin",
		"Westmount",
		"Whitehall",
		"Whitney",
		"Wickenburg",
		"Wilkinsburg",
		"Windsor",
		"Wittmann",
		"Woolwich",
		"Wörth am Rhein",
		"Youngtown"})
	String[] lstCities();
	
	@DefaultStringArrayValue({"Select","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"})
	String[] lstDays();
	
	@DefaultStringArrayValue({"Select","00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"})
	String[] lstHours();

	@DefaultStringValue("Parametrizacion: pesos a aplicar para calculo de similitud")
	String labelContentParameters();
}



