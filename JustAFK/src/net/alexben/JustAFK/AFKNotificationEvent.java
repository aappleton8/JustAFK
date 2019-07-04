package net.alexben.JustAFK;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AFKNotificationEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
    private String reason;
    private boolean oldStatus; 
    private boolean newStatus; 
    private boolean certainty; 
    private Player player; 
    
    boolean cancelled; 
    
    public AFKNotificationEvent(Player AFKPlayer, boolean AFKCertainty, boolean oldAFKStatus, boolean newAFKStatus, String AFKReason) {
    	player = AFKPlayer; 
    	certainty = AFKCertainty; 
    	oldStatus = oldAFKStatus; 
    	newStatus = newAFKStatus; 
    	reason = AFKReason; 
    	cancelled = false; 
    }
    
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public Player getPlayer() {
		return player; 
	}
	
	public boolean getCertainty() {
		return certainty; 
	}
	
	public boolean getOldStatus() {
		return oldStatus; 
	}
	
	public boolean getNewStatus() {
		return newStatus; 
	}
	
	public String getReason() {
		return reason; 
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0; 
		
	}

}
