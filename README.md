# MobileUSOZ


opis działania programu:
- Calendar: Pozwala użytkownikowi na dodawanie/kasowanie własnych notatek na każdy dzień w roku. Wyświetlane są również wydarzenia które zostały zarejestrowane przed administratora.
- UserProfile: Wyświetla informacje użytkownika takie jak: Imie, nazwisko, data urodzenia, uczelnia oraz pasje.
- UserAccount: Pozwala na założenia konta użytkownikowi chcącemu użytkować aplikacje. Możliwa jest standardowa rejestracja przez email i hasło oraz logowanie przez Facebook'a lub konto Google.
- Maps: wyswietla mapę zawierającą pinezki z ciekawymi miejscami w krakowie. Miejsca są z 4 kategorii: Rozrywka, Nauka, Kawiarnia, Sport
- Lecturers: Wyswietla listę z wykładowcami/ćwiczniowcami z uczelni w krakowie, po kliknięciu na danego wykładowcę/ćwiczeniowca przechodzi do strony z informacjami o danej osobie.
- LecturerPage: Strona wykładowcy/ćwiczeniowca, zawiera informacje odnośnie prowadzonych przedmiotów przez daną osobę. Pozwala również na wystawienie oceny danemu pracownikowi.

sposób wywołania:
    Shift+F10 w Android Studio 3.4.1 lub nowszym

wykaz z opisem ważniejszych zmiennych:
    FirebaseAuth mAuth - Przechowuje aktualną sesje użytkownika Firebase
    FirebaseUser user - Przechowuje dane zalogowanego użytkownika
    FirebaseFirestore db - Referencja do bazy danych (Firestore)
    DrawerLayout drawer - Glowny layout uzywany w kazdym activity z paska zadan
    NavigationView navigationView - nawigacja aplikacji pozwalajaca przemieszczać się pomiędzy aktywnościami
    Toolbar toolbar - Pasek narzedzi
    RecyclerViewAdapter adapter -  Przygotowywuje RecyclerView layout do wyświetlenia danych
    BroadcastReceiver networkChangeReceiver - monitoruje zmianę stanu połączenia internetowego

opis plików wejścia/wyjścia:
    brak plików wejścia/wyjścia

nazwy używanych podprogramów:
    Google Maps
    Firebase(Firestore)

nazwy wszelkich specjalnych metod, które zostały użyte, wraz ze wskazaniem, gdzie można znaleźć dalsze informacje:
    public abstract void onMapReady (GoogleMap googleMap) https://developers.google.com/android/reference/com/google/android/gms/maps/OnMapReadyCallback
    serialize(Serializable obj) https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/SerializationUtils.html
    deserialize(byte[] objectData) https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/SerializationUtils.html
    getBytes() https://firebase.google.com/docs/storage/android/download-files
    putBytes(byte[] array) https://firebase.google.com/docs/storage/android/upload-files
    get() https://firebase.google.com/docs/firestore/query-data/get-data
    runTransaction() https://firebase.google.com/docs/firestore/manage-data/transactions
    mGoogleSignInClient.getSignInIntent() https://firebase.google.com/docs/auth/android/google-signin
    FirebaseAuth https://firebase.google.com/docs/auth/android/facebook-login
    handleFacebookAccessToken https://developers.facebook.com/docs/facebook-login/access-tokens/
	
	
wymagania sprzętowe i systemowe:
    wersja systemu: android 8 lub nowszy
    minimalna ilość pamięci ram: 250MB
    minimalna ilość miejsca na dysku: 200MB

opis specjalnych poleceń dla użytkownika:
Ekran logowania:
Zaloguj - po wpisaniu loginu i hasła loguje nas do firebase 
Zarejestruj - pozwala zarejestrować nowe konto użytkownika przy pomocy email'a i hasła
Sign In(Google) -  logowanie  przy pomocy konta Google.
Continue with Facebook - logowanie przy pomocy konta Facebook
Menu:
Profil - przenosi nas do  profilu użytkownika
Kalendarz - pozwala przenieść nas do kalendarza gdzie możemy odczytać/dodać swoje notatki lub wydarzenia
Wykładowcy - przenosi nas do bazy danych wykładowców gdzie możemy odczytać ocene wykładowców
Mapa - pokazuje nam mapę naszej okolicy gdzie są zaznaczone ważne miejsca lub wydarzenia
Wyloguj - wylogowywuje nasze konto z aplikacji
Ekran Profilu użytkownika:
Obraz ołówka obok profilu - pozwala zmienić swoje dane osobowe
Ekran zmiany danych osobowych:
Zapisz - nadpisuje stare dane nowowprowadzonymi 
Kalendarz:
Dodaj Notatkę - pozwala na dodanie nowej notatki przez użytkownika
Ekran dodawania nowej notatki:
Zapisz - zapisuje w bazie danych nową notatkę
Ekran wyświetlenia notatek w danym dniu:
"X" - Usuwa notatkę. Jeśli nie ma notatek oraz wydarzeń przenosi do widoku miesięcy
Wykładowcy:
Oceń prowadzącego - Dodaje nową ocene do bazy danych, ocena może być dodana tylko raz
Mapa:
Pokaż/ukryj pinezki - ukrycie lub pokazywanie pinezek podczas wyświelania mapy
   

autor i data powstania (data modyfikacji/numer wersji):
    Autorzy:
        Krzysztof Szczurek
        Michał Piotrowski
        Piotr Opyd
        Przemysław Przybysz
        Mariusz Bieda
        Konrad Padewski

    Data powstania:
        18.03.2019r. v. 1.0
