package
{
  import flash.events.Event;

  public class SearchEvent extends Event
  { 
    public function SearchEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
    {
      super(type, bubbles, cancelable);
    }
    
  }
}