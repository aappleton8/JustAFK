package net.alexben.JustAFK;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class AddPlaceholders extends PlaceholderExpansion {
	
	private static JustAFK plugin; 
	
	public AddPlaceholders(JustAFK instance) {
		plugin = instance; 
	}
	
	@Override
	public boolean persist(){
	    return true;
	}
	
	@Override
    public boolean canRegister(){
        return true;
    }

	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public String getIdentifier() {
		return plugin.getDescription().getName();
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion(); 
	}
	
	@Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        // %JustAFK_afk%
        if(identifier.equalsIgnoreCase("afk")){
            return JUtility.isAway(player, true) ? "away" : "not away"; 
        }

        // %JustAFK_afkexempt%
        if(identifier.equalsIgnoreCase("afkexempt")){
            return JUtility.isExempt("afk", player) ? "afk exempt" : "not afk exempt";
        }
        
        // %JustAFK_kickexempt%
        if(identifier.equalsIgnoreCase("kickexempt")){
            return JUtility.isExempt("kick", player) ? "kick exempt" : "not kick exempt";
        }
        
        // %JustAFK_lightningexempt%
        if(identifier.equalsIgnoreCase("lightningexempt")){
            return JUtility.isExempt("lightning", player) ? "lightning exempt" : "not lightning exempt";
        }
 
        // We return null if an invalid placeholder was provided 
        return null;
    }

}
