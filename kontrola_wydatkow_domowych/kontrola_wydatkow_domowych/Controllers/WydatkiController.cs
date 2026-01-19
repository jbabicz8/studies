using kontrola_wydatkow_domowych.Models;
using kontrola_wydatkow_domowych.Services;
using Microsoft.AspNetCore.Mvc;

[Route("api/[controller]")] // ROUTING DLA KONTROLERA API
[ApiController] // DEFINICJA KONTROLERA API DLA WYDATKÓW i KATEGORII
public class WydatkiController : ControllerBase // KONTROLER PODSTAWOWY
{
    private static readonly BudzetDomowy _budzet = new(); // statyczna instancja budżetu domowego

    [HttpGet] // GET: api/Wydatki
    public ActionResult<IEnumerable<Wydatek>> Get() => Ok(_budzet.Wydatki); // ZWRACANIE LISTY WYDATKÓW

    [HttpPost] // POST: api/Wydatki
    public IActionResult Post([FromBody] Wydatek w) // DODAWANIE NOWEGO WYDATKU
    {
        try // OBSŁUGA WYJĄTKÓW
        {
            _budzet.DodajWydatek(w);
            return Ok();
        }
        catch (WalidacjaBudzetuException ex) // PRZECHWYTYWANIE WYJĄTKÓW
        {
            return BadRequest(ex.Message);
        }
    }

    [HttpPut("{id}")] // PUT: api/Wydatki/5
    public IActionResult Put(int id, [FromBody] Wydatek w) // EDYTOWANIE ISTNIEJĄCEGO WYDATKU
    {
        w.Id = id;
        _budzet.EdytujWydatek(w);
        return Ok();
    }

    [HttpDelete("{id}")] // DELETE: api/Wydatki/5
    public IActionResult Delete(int id) // USUWANIE WYDATKU
    {
        _budzet.UsunWydatek(id);
        return Ok();
    }

    [HttpGet("Kategorie")] // GET: api/Wydatki/Kategorie
    public ActionResult<IEnumerable<Kategoria>> GetKategorie() => Ok(_budzet.Kategorie); // ZWRACANIE LISTY KATEGORII

    [HttpPost("Kategorie")] // POST: api/Wydatki/Kategorie
    public IActionResult PostKategoria([FromBody] Kategoria k) // DODAWANIE NOWEJ KATEGORII
    {
        _budzet.DodajKategorie(k);
        return Ok();
    }

    [HttpDelete("Kategorie/{id}")] // DELETE: api/Wydatki/Kategorie/5
    public IActionResult DeleteKategoria(int id) // USUWANIE KATEGORII
    {
        _budzet.UsunKategorie(id);
        return Ok();
    }

    [HttpPut("Kategorie/{id}")] // PUT: api/Wydatki/Kategorie/5
    public IActionResult UpdateKategoria(int id, [FromBody] Kategoria k) // EDYTOWANIE KATEGORII
    {
        k.Id = id;
        _budzet.EdytujKategorie(k);
        return Ok();
    }

    [HttpGet("Limit")] // GET: api/Wydatki/Limit
    public ActionResult<decimal> GetLimit() => Ok(_budzet.LimitOgolny); // ZWRACANIE LIMITU BUDŻETU

    [HttpPost("Limit")] // POST: api/Wydatki/Limit
    public IActionResult SetLimit([FromBody] decimal nowyLimit) // USTAWIANIE LIMITU BUDŻETU
    {
        _budzet.LimitOgolny = nowyLimit;
        _budzet.ZapiszStan();
        return Ok();
    }
}