package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.InvalidDateException;
import ggc.core.WarehouseManager;
import ggc.core.classes.Date;
//FIXME import classes
import ggc.core.exception.BadEntryException;


/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("timeAdd", "Quantos dias quer avançar no tempo?: ");
    //FIXME add command fields
  }

  @Override
  protected final void execute() throws CommandException, InvalidDateException {

    try{

    Date.add(integerField("timeAdd"));

    }catch (BadEntryException e){

      throw new InvalidDateException(integerField("timeAdd"));
    }
    
  }

}
