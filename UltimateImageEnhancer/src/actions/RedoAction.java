/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Electronics
 */
public class RedoAction extends AbstractAction{
    protected UndoAction undoAction;
    protected RedoAction redoAction;
    protected UndoManager undoManager ;  
 
    public RedoAction(){       
        super("Redo");
        
            setEnabled(false);
    }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }
public void setUndoAction(UndoAction undo,UndoManager undoM){
    undoAction=undo;
    this.undoManager=undoM;
}
        public void updateRedoState() {
            if(undoManager!=null){
                 if (undoManager.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undoManager.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
            }
           
        }
}
