namespace kontrola_wydatkow_domowych.Services
{
    // ten interfejs oznacza, że każdy model, który go implementuje, musi mieć przycisk Zapisz i Wczytaj. 
    // litera T - typ generyczny, który musi być klasą (where T : class)
    public interface IRepozytorium<T> where T : class
    {
        void Zapisz(T dane);
        T? Wczytaj();
    }
    // gdybyśmy uznali, że nie chcemy zapisać danych do pliku JSON, tylko do np. w bazie SQL, to zmieniamy tylko to co dzieje się
    // wewnątrz klasy repozytoriumJSON (lub inna nazwa) i implementujemy tam odpowiednią logikę zapisu i odczytu. 
}

