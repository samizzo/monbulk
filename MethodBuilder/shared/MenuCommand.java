package daris.Monbulk.MethodBuilder.shared;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import daris.Monbulk.MethodBuilder.client.event.MenuChangeEvent;

public class MenuCommand implements Command{
	    HandlerManager eventBus;
		String CmdName;
		public MenuCommand(String cmdName,HandlerManager tmpBus)
		{
			CmdName = cmdName;
			eventBus = tmpBus;
		}
		@Override
		public void execute() {
			// TODO Auto-generated method stub
			//eventBus.
			eventBus.fireEvent(new MenuChangeEvent(CmdName));
		}
		
}

