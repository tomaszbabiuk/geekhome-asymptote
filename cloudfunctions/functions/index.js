const functions = require('firebase-functions');

exports.showTermsEN = functions.https.onRequest((request, response) => {
    var html =
    '<!DOCTYPE html>' +
    '<html lang="en">' +
    '<head>' +
    '   <meta charset="utf-8">' +
    '   <meta http-equiv="X-UA-Compatible" content="IE=edge">' +
    '   <meta name="viewport" content="width=device-width, initial-scale=1">' +
    '   <title>geekHOME - terms and conditions</title>' +
    '   <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">' +
    '   <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>' +
    '   <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js" integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous"></script>' +
    '   <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>' +
    '</head>' +
    '<body>' +
    '   <div class="container">' +
    '       <h1>Welcome to GeekHOME!</h1>' +
    '       <p>Thanks for using our products and services (“Services”). The Services are provided by GeekHOME Sp z o.o. (“GeekHOME”), located at 184 Dziekanowice, 32-086 Dziekanowice, Lesser Poland, Cracow.</p>' +
    '       <p>By using our Services, you are agreeing to these terms. Please read them carefully.</p>' +

    '       <h1>Terms and Conditions</h1>' +
    '       <h3>Our Warranties and Disclaimers</h3>' +
    '       <p>We provide our Services using a commercially reasonable level of skill and care and we hope that you will enjoy using them. But there are certain things that we do not promise about our Services.</p>' +
    '       <p>NEITHER GEEKHOME NOR ITS SUPPLIERS OR DISTRIBUTORS MAKES ANY SPECIFIC PROMISES ABOUT THE SERVICES. FOR EXAMPLE, WE DO NOT MAKE ANY COMMITMENTS ABOUT THE CONTENT WITHIN THE SERVICES, THE SPECIFIC FUNCTIONS OF THE SERVICES OR THEIR RELIABILITY, AVAILABILITY OR ABILITY TO MEET YOUR NEEDS. WE PROVIDE THE SERVICES “AS IS”.</p>' +
    '       <p>SOME JURISDICTIONS PROVIDE FOR CERTAIN WARRANTIES, LIKE THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. TO THE EXTENT PERMITTED BY LAW, WE EXCLUDE ALL WARRANTIES.</p>' +
    '       <h3>Liability for our Services</h3>' +
    '       <p>WHEN PERMITTED BY LAW, GEEKHOME AND GEEKHOME’S SUPPLIERS AND DISTRIBUTORS WILL NOT BE RESPONSIBLE FOR LOST PROFITS, REVENUES OR DATA, FINANCIAL LOSSES OR INDIRECT, SPECIAL, CONSEQUENTIAL, EXEMPLARY OR PUNITIVE DAMAGES.</p>' +
    '       <p>TO THE EXTENT PERMITTED BY LAW, THE TOTAL LIABILITY OF GEEKHOME AND ITS SUPPLIERS AND DISTRIBUTORS FOR ANY CLAIMS UNDER THESE TERMS, INCLUDING FOR ANY IMPLIED WARRANTIES, IS LIMITED TO THE AMOUNT THAT YOU PAID US TO USE THE SERVICES (OR, IF WE CHOOSE, TO SUPPLYING YOU WITH THE SERVICES AGAIN).</p>' +
    '       <p>IN ALL CASES, GEEKHOME AND ITS SUPPLIERS AND DISTRIBUTORS WILL NOT BE LIABLE FOR ANY LOSS OR DAMAGE THAT IS NOT REASONABLY FORESEEABLE.</p>' +

    '       <h1>Privacy policy</h1>' +
    '       <p>When you use GeekHOME automation and IoT system, you trust us with your information. This Privacy Policy is meant to help you understand what data we collect, why we collect it and what we do with it.' +
    '       <h3>Definitions</h3>' +
    '       <p><b>House configuration schema</b> - contains all necessary information about how floors and rooms are located in the building, devices, automation conditions, modes, alerts and other configuration items defined by the user during system setup.</p>' +
    '       <p><b>Third party accounts data</b> - all information required to third party services to work (eg. cloud access, controlling the building using email or internet messaging) defined by the user during system setup.</p>' +
    '       <p><b>Sensor readings</b> - all data readings from GeekHOME controlled sensors and actuators.</p>' +
    '       <p><b>Cloud configuration backup</b> - a internet service providing configuration backup in internet network</p>' +
    '       <p><b>Cloud device access</b> - a internet service providing possibility to control devices from any place with internet connection</p>' +
    '       <p><b>Personal data</b> - all the information about the user like firstname, lastname, email address, geekHOME account password and acquired license data</p>' +
    '       <h3>Information that we collect</h3>' +
    '       <p>While using GeekHOME automation and IoT system, different types of information are collected: from house schema to third party account data.</p>' +
    '       <p>We collect information in the following ways:/p>' +
    '       <p><b>GeekHOME Asymptote sensor control app</b> – the app collects third party accounts data (user account) and paired sensors metadata. All data is secured and stored in the Google Firebase cloud, managed by geekHOME Administrator.</p>' +
    '       <p><b>GeekHOME home automation server</b> – the app collects house schema, third party accounts configuration and sensors data. House schema is stored in two places: locally and in the cloud (as a backup). All settings (third party accounts data) are stored locally only.</p>' +
    '       <p><b>GeekHOME Pilot</b> - the home control app collects are house schema and xmpp account data (which is required to connect controlled house with GeekHOME Pilot app together via internet network). All data is stored locally on device (smartphone or tablet) memory.</p>' +
    '       <p><b>GeekHOME license</b> - contains user data that are required to validate the license. The data is stored in the cloud only. We restrict access to personal information to GeekHOME employees only.</p>' +
    '       <h3>How we use information that we collect</h3>' +
    '       <p>We collect only the information required to GeekHOME to work correctly. We don\'t process this information to extract details that are not relevant to the GeekHOME automation system during it\'s normal work.</p>' +
    '       <h3>Information security</h3>' +
    '       <p>We work hard to protect GeekHOME company and our users from unauthorised access to or unauthorised alteration, disclosure or destruction of information that we hold. In particular:</p>' +
    '       <p>&#8226; We encrypt many of our services using SSL.<br/>' +
    '       &#8226; The devices that are sensitive to security (eg. code locks or gates) are protected by additional code which is required to change its state.<br/>' +
    '       &#8226; We review our information collection, storage and processing practices, including physical security measures, to guard against unauthorised access to system.<br/>' +
    '       &#8226; We restrict access to personal information to GeekHOME employees, contractors and agents who need to know that information in order to process it for us and who are subject to strict contractual confidentiality obligations. They may be disciplined or their contract terminated if they fail to meet these obligations.<br/>' +
    '       <h3>Changes</h3>' +
    '       <p>Our Privacy Policy may change from time to time. We will not reduce your rights under this Privacy Policy without your explicit consent. We will post any Privacy Policy changes on http://geekhome.eu page and, if the changes are significant, we will provide a more prominent notice (including, for certain services, email notification of Privacy Policy changes).</p>' +

    '   </div>' +
    '</body>' +
    '</html>';

    response.send(html);
});

exports.showTermsPL = functions.https.onRequest((request, response) => {
    var html =
    '<!DOCTYPE html>' +
    '<html lang="en">' +
    '<head>' +
    '   <meta charset="utf-8">' +
    '   <meta http-equiv="X-UA-Compatible" content="IE=edge">' +
    '   <meta name="viewport" content="width=device-width, initial-scale=1">' +
    '   <title>geekHOME - terms and conditions</title>' +
    '   <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">' +
    '   <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>' +
    '   <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js" integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous"></script>' +
    '   <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>' +
    '</head>' +
    '<body>' +
    '   <div class="container">' +
    '       <h1>Witamy w GeekHOME!</h1>' +
    '       <p>Dziękujemy za korzystanie z naszych usług i aplikacji („Usług”). Usługi są świadczone przez firmę GeekHOME Sp z o.o. („GeekHOME”), której siedziba znajduje się pod adresem Dziekanowice 184, 32-086 Dziekanowice, woj. małopolskie, Polska.</p>' +
    '       <p>Korzystając z Usług, użytkownik akceptuje niniejsze warunki. Prosimy o ich dokładne przeczytanie.</p>' +

    '       <h1>Warunki korzystania</h1>' +
    '       <h3>Gwarancje i wyłączenia odpowiedzialności</h3>' +
    '       <p>Usługi świadczymy przy wykorzystaniu uzasadnionego pod względem handlowym poziomu umiejętności i zaangażowania. Mamy nadzieję, że korzystanie z nich będzie przyjemne. Mimo to nie możemy w związku z Usługami złożyć pewnych obietnic.</p>' +
    '       <p>FIRMA GEEKHOME ANI JEJ DOSTAWCY I DYSTRYBUTORZY NIE SKŁADAJĄ ŻADNYCH KONKRETNYCH OBIETNIC ZWIĄZANYCH Z USŁUGAMI, INNYCH NIŻ WYRAŹNIE WYMIENIONE W NINIEJSZYCH WARUNKACH ORAZ WARUNKACH DODATKOWYCH. NA PRZYKŁAD NIE OKREŚLAMY ŻADNYCH SWOICH ZOBOWIĄZAŃ W ZAKRESIE TREŚCI ZNAJDUJĄCYCH SIĘ W USŁUGACH, KONKRETNYCH FUNKCJI, NIEZAWODNOŚCI ANI DOSTĘPNOŚCI USŁUG, JAK RÓWNIEŻ MOŻLIWOŚCI SPEŁNIENIA PRZEZ NIE POTRZEB UŻYTKOWNIKA. USŁUGI SĄ ŚWIADCZONE „W TAKIM STANIE, W JAKIM SIĘ ZNAJDUJĄ”.</p>' +
    '       <p>W NIEKTÓRYCH JURYSDYKCJACH PRZEWIDZIANO KONIECZNOŚĆ UDZIELENIA PEWNYCH GWARANCJI, NA PRZYKŁAD DOROZUMIANEJ GWARANCJI WARTOŚCI HANDLOWEJ, PRZYDATNOŚCI DO OKREŚLONEGO CELU LUB NIENARUSZANIA PRAW. W ZAKRESIE DOZWOLONYM PRZEZ PRAWO WYŁĄCZAMY WSZYSTKIE GWARANCJE.</p>' +
    '       <h3>Odpowiedzialność za Usługi</h3>' +
    '       <p>JEŚLI JEST TO DOZWOLONE PRZEZ PRAWO, FIRMA GEEKHOME ANI JEJ DOSTAWCY I DYSTRYBUTORZY NIE PONOSZĄ ODPOWIEDZIALNOŚCI ZA UTRACONE ZYSKI, PRZYCHODY ANI DANE, STRATY FINANSOWE, SZKODY POŚREDNIE, SZCZEGÓLNE, WTÓRNE ANI MORALNE, A TAKŻE ODSZKODOWANIA Z TYTUŁU STRAT MORALNYCH.</p>' +
    '       <p>W ZAKRESIE DOZWOLONYM PRZEZ PRAWO CAŁKOWITA ODPOWIEDZIALNOŚĆ FIRMY GEEKHOME, JEJ DOSTAWCÓW I DYSTRYBUTORÓW Z TYTUŁU WSZELKICH ROSZCZEŃ NA MOCY NINIEJSZYCH WARUNKÓW, W TYM Z TYTUŁU WSZELKICH DOROZUMIANYCH GWARANCJI I RĘKOJMI, JEST OGRANICZONA DO KWOTY ZAPŁACONEJ NAM PRZEZ UŻYTKOWNIKA ZA KORZYSTANIE Z USŁUG (LUB, JEŚLI PODEJMIEMY TAKĄ DECYZJĘ, OGRANICZONA DO PONOWNEGO ŚWIADCZENIA PRZEZ NAS USŁUG UŻYTKOWNIKOWI).</p>' +
    '       <p>W ŻADNYM PRZYPADKU FIRMA GEEKHOME, JEJ DOSTAWCY ANI DYSTRYBUTORZY NIE PONOSZĄ ODPOWIEDZIALNOŚCI ZA JAKIEKOLWIEK STRATY BĄDŹ SZKODY, KTÓRYCH NIE MOŻNA PRZEWIDZIEĆ PRZY ZASTOSOWANIU UZASADNIONYCH ŚRODKÓW.</p>' +

    '       <h1>Polityka prywatności</h1>' +
    '       <p>Korzystając z systemu sterowania GeekHOME (systemu automatyki budynkowej lub aplikacji sterujących) powierzasz nam swoje informacje. Niniejsza Polityka prywatności służy jako pomoc w zrozumieniu, jakie dane są zbierane, w jakim celu oraz do czego są wykorzystywane. Te informacje są ważne, dlatego prosimy o dokładne zapoznanie się z tym dokumentem. Treść polityki prywa tności została znacznie uproszczona, jednak w przypadku nieznajomości takich kluczowych terminów, jak czujnik, odczyty stanów, adres IP, chmura, warto najpierw przeczytać ich definicje.</p>' +
    '       <h3>Definicje</h3>' +
    '       <p><b>Schemat konfiguracyjny budynku</b> - zawiera wszystkie informacje o rozmieszczeniu pięter i pokojów w budynku, urządzeniach, warunkach automatyki, trybach, alarmach oraz pozstałych rzeczach podawanych przez użytkownika podczas konfiguracji systemu (za wyjątkiem sekcji “Ustawienia”).</p>' +
    '       <p><b>Dane kont systemów trzecich</b> - wszystkie dane niezbędne do funkcjonowania usług dodatkowych (np. dostęp do chmury, sterowanie domem za pomocą poczty elektronicznej lub komunikatora internetowego) podawanych przez użytkownika podczas konfiguracji systemu w sekcji “Ustawienia”</p>' +
    '       <p><b>Odczyty czujników</b> - wszystkie informacje przekazywane przez urządzenia, którymi system GeekHOME steruje, lub na podstawie których podejmuje akcje sterowania (tj. wszelkiego rodzaju dane z czujników lub kart wykonawczych).</p>' +
    '       <p><b>Kopia konfiguracji w chmurze</b> - usługa internetowa dostępna za pomocą sieci internet polegająca na przechowywaniu kopii schematu konfiguracyjnego na serwerze dostępnym przez sieć internet</p>' +
    '       <p><b>Dane osobowe</b> - informacje o użytkowniku takie jak imię i nazwisko, adres email, hasło do kopii bezpieczeństwa oraz dodatkowe informacje na temat licencji wykupionej przez użytkownika</p>' +
    '       <h3>Gromadzone informacje</h3>' +
    '       <p>Podczas normalnej pracy z systemem GeekHOME gromadzone są różne dane: od schematu konfiguracyjnego budynku, po dane na temat kont systemów trzecich.</p>' +
    '       <p>Gromadzimy informacje w następujący sposób:</p>' +
    '       <p><b>Aplikacja sterująca GeekHOME Asymptote</b> – aplikacja przechowuje wszystkie niezbędne dane związane ze danymi kont systemów trzecich (konto użytkownika) oraz metadane sparowanych czujników. Wszystkie te dane są bezpiecznie przechowywane w chmurze Google Firebase i zarządzane przez administratora systemu geekHOME.</p>' +
    '       <p><b>Aplikacja sterująca GeekHOME Server</b> – aplikacja przechowuje wszystkie niezbędne dane związane ze schematem konfiguracyjnym budynku, danymi kont systemów trzecich oraz odczytami czujników. Schemat konfiguracyjny budynku przechowywany jest lokalnie (na karcie pamięci na której znajduje się system GeekHOME) oraz w chmurze (kopia bezpieczeństwa). Wszystkie ustawienia przechowywane są wyłącznie lokalnie.</p>' +
    '       <p><b>Aplikacja GeekHOME Pilot</b> - aplikacja przechowuje dane związane ze schematem konfiguracyjnym budynku oraz ustawienia konta XMPP (niezbędnego do komunikacji aplikacje sterującej z budynkiem). Dane te przetrzymywane są wyłącznie w lokalniej pamięci na telefonie lub tablecie na którym zainstalowana jest aplikacja.</p>' +
    '       <p><b>Licencja systemu GeekHOME</b> - zawiera dane osobowe użytkownika, niezbędne do weryfikacji legalności systemu. Dane te przechowywane są w chmurze. Ma do nich dostęp jedynie pracownik firmy GeekHOME.</p>' +
    '       <h3>Sposoby wykorzystania zgromadzonych informacji</h3>' +
    '       <p>Wszystkie informacje, które użytkownik wprowadza do systemu GeekHOME są niezbędne do prawidłowego działania systemu. Firma GeekHOME nie przetwarza danych użytkownika w celu wydobycia z nich informacji innych, niż te niezbędne do prawidłowej pracy systemu GeekHOME.</p>' +
    '       <h3>Bezpieczeństwo informacji</h3>' +
    '       <p>Dokładamy wszelkich starań, aby chronić firmę GeekHOME i użytkowników przed nieuprawnionym dostępem, nieautoryzowaną modyfikacją, ujawnieniem oraz zniszczeniem informacji znajdujących się w posiadaniu GeekHOME. W szczególności:</p>' +
    '       <p>&#8226; Stosujemy szyfrowanie SSL.<br/>' +
    '       &#8226; Urządzenia których działanie w budynku jest krytyczne dla bezpieczeństwa (np. szyfratory alarmowe lub bramy) są zabezpieczone dodatkowym kodem, niezbędnym do zmiany stanu ich pracy<br/>' +
    '       &#8226; Kontrolujemy nasze metody gromadzenia, przechowywania i przetwarzania informacji, w tym fizyczne środki bezpieczeństwa, aby chronić przed nieuprawnionym dostępem do systemu.<br/>' +
    '       &#8226; Dostępu do danych osobowych udzielamy jedynie tym pracownikom, kontrahentom i przedstawicielom firmy GeekHOME, którzy muszą mieć do nich dostęp, aby przetwarzać je na potrzeby GeekHOME (na przykład sprawdzić ważność licencji). Ponadto na mocy umowy są oni zobowiązani do zachowania ścisłej poufności, a w przypadku niewypełnienia tych zobowiązań mogą ponieść konsekwencje, łącznie z zakończeniem współpracy.<br/>' +
    '       <h3>Zmiany</h3>' +
    '       <p>Polityka prywatności GeekHOME może czasami ulegać zmianom (np. w momencie wprowadzania nowych usług do systemu). Prawa użytkownika wynikające z niniejszej Polityki prywatności nie zostaną ograniczone bez wyraźnej zgody użytkownika. Wszelkie zmiany Polityki prywatności będą publikowane na stronie http://geekhome.eu, a o istotnych zmianach będziemy informować w bardziej widoczny sposób (w przypadku niektórych usług łącznie z wysłaniem odpowiedniego powiadomienia e-mail).</p>' +
    '   </div>' +
    '</body>' +
    '</html>';

    response.send(html);
});
