namespace kontrola_wydatkow_domowych.Models
{
    // klasa abstrakcyjna - nie można tworzyć jej instancji bezpośrednio
    public abstract class ElementBudzetu
    {
        public int Id { get; set; } // unikalny identyfikator elementu budżetu

        // wspólna cecha: każdy element budżetu operuje na jakiejś kwocie
        public decimal Wartosc { get; set; } // kwota powiązana z elementem budżetu
    }
}