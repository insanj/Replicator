//TODO: Modify this plugin to make MINESHAFTS, become a REPLICATOR, replicate structures, and more.
//TODO: Make Permissions checking at each command better...

/*
 * THE REPLICATOR PLUGIN [version 1.4, "TANGENT"]
 * The first plugin crafted by INSANJ. (www.insanj.com/replicator)
 * CREATED: 5/14/11          LAST EDITED: 6/20/11
 * 
 * To be used with the current CraftBukkit build #860.
 *
 * Special thanks to:
 *   Adamki11s for his tips, but, most of all
 *   the suggestion to use his Zombie mod as a reference.
 *
 *   Yottabyte for simple bug squashing.
 *   
 *   Matt (aka: heyredhat) for his incredible implementation
 *   of a SPHERE. Man, only 9 lines of code for a perfect
 *   sphere at an instant, with only 30 minutes to come
 *   up with it.
 *
 * This plugin's permanent home is in the
 * REPLICATOR world on the insanj servers:
 * 	       minecraft.insanj.com
 * 
 */

package me.insanj.replicator;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;

import java.io.BufferedWriter;
import java.io.File; 
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;

//The main method/class.
public class replicator extends JavaPlugin
{
	//Sets the console, the "listeners", and such.
	private static final Logger log = Logger.getLogger("Minecraft");
	public PermissionHandler permissionHandler;
	//private final replicatorPlayerListener playerListener = new replicatorPlayerListener(this);

	private static final String version = "1.4 [\"TANGENT\"]";
	private static Block diamondTarget;
	private static Block cageTarget;
	private static Block sphereTarget;
	private static Block readTarget;
	
	public static boolean permissions;
	
	public static ArrayList<String> text = new ArrayList<String>();
	
	public static String directory = "plugins/Replicator";
	private JavaPlugin replicator = this;
	
	private static int sphereRadius;
		
	public boolean isValidId( int id ) 
	{
	    return id != 0 && Material.getMaterial(id) != null;
	}
	
	//When the plugin is enabled, this method is called.
	@Override
	public void onEnable()
	{
		//PluginManager pm = getServer().getPluginManager();
		//pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
		
		log.info("{Replicator} plugin version " + version + " has successfully started.");
		setupPermissions();
		
		text.add("Use this folder to store your /read files!");
		text.add("\nUse the format: [item id], [x additive], [y additive], [z additive].");
		text.add("\nFor example, to create a wood block a block above where you're looking:\t5, 0, 1, 0");
		text.add("\nMore info in the readme file inside the .jar package!");

		//An attempt to create a new directory, with a file, no matter the directory system.
		if( !(new File(directory).exists()) )
		{
			try
			{
				new File(directory).mkdir();
				File file = new File("plugins/Replicator/readme.txt");
				Writer output = new BufferedWriter(new FileWriter(file));
				
				for(int i = 0; i < text.size(); i++)
					output.write(text.get(i));
								
			 	output.close();
			}
			
				catch (Exception e1)
				{
					log.info("{Replicator} had a problem creating/storing in the directory! Error: " + e1.getMessage());
				}
		}//end if
		
	}//end method onEnable()
	
	//When the plugin is disabled, this method is called.
	@Override
	public void onDisable() 
	{
		log.info("{Replicator} plugin version " + version + " disabled.");
		
	}//end method onDisable()
	
	//For permissions.
	private void setupPermissions() 
	{
	      Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

	      if (this.permissionHandler == null) 
	      {
	          if (permissionsPlugin != null) 
	              this.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	          
	          else 
	          {
	              log.info("{Replictor} could not detect a Permissions system, defaulting to OP usage.");
	              permissions = false;
	          } 
	      }
	      
	      
	  }//end setupPermissions();
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) 
	{
    	Player player = (Player)sender;	
    	int currID = 5;
    	
		//Runs the command execution method.
		if( commandLabel.equalsIgnoreCase("replicator"))
		{
			if( args.length > 0 )
			{
				if( args[0].equalsIgnoreCase("diamond")  && (sender.isOp() || permissionHandler.has(player, "replicator.diamond") ))
				{
					if (args.length == 1 )
						diamond(player, 5, null);
						
					else if (args.length == 2)
					{
						if( args[1].equalsIgnoreCase("undo") )
						{
							if(diamondTarget == null)
								player.sendMessage(ChatColor.RED + "First you have to make a diamond, dummy!");
							else
								undoDiamond( (Player) sender );
						}
						
						try
						{
							currID = Integer.parseInt(args[1]);
							if( isValidId(currID) )
								diamond(player, currID, null);
							else
								player.sendMessage(ChatColor.RED + "Not a valid item id #!");
						}//end try
						
						catch (NumberFormatException ex)
						{							
							player.sendMessage(ChatColor.RED + "The id must be an integer! " + ChatColor.YELLOW + "/replicator diamond [id#]");
						}
							
					 }//end else if
					
					 else
						 player.sendMessage(ChatColor.RED + "Incorrect argument! Need help, " + player.getName() + "? Type in /replicator .");	
					
				  }//end diamond if
				
				else if( args[0].equalsIgnoreCase("cage")  && (sender.isOp() || (permissions == true && permissionHandler.has(player, "replicator.cage")) ) )
				{
					if (args.length == 1)
						cage(player, 20, null);
						
					else if (args.length == 2)
					{
						if( args[1].equalsIgnoreCase("undo") )
						{
							if(cageTarget == null)
								player.sendMessage(ChatColor.RED + "First you have to make a cage, dummy!");
							else
								undoCage( (Player) sender );
						}
						
						try
						{
							currID = Integer.parseInt(args[1]);
							if( isValidId(currID) )
								cage(player, currID, null);
							else
								player.sendMessage(ChatColor.RED + "Not a valid item id #!");
						}
						
						catch (NumberFormatException ex)
						{							
							player.sendMessage(ChatColor.RED + "The id must be an integer! " + ChatColor.YELLOW + "/replicator cage [id#]");
						}
						
					}//end else if
						
					else
						player.sendMessage(ChatColor.RED + "Incorrect argument! Need help, " + player.getName() + "? Type in /replicator .");	
					
				}//end cage else if
				
				else if( args[0].equalsIgnoreCase("sphere") && (sender.isOp() || (permissions == true && permissionHandler.has(player, "replicator.sphere")) ))
				{
					
					if( args.length == 2)
					{
						if( args[1].equalsIgnoreCase("undo") )
						{
							if(sphereTarget == null)
								player.sendMessage(ChatColor.RED + "First you have to make a sphere, dummy!");
						
							else
								undoSphere( (Player) sender );
						}
						
						else
							player.sendMessage(ChatColor.RED + "Incorrect syntax. Type in /replicator for help!");
					}
					
					else if (args.length == 3)
					{
						try
						{
							currID = Integer.parseInt(args[1]);
							int radius = Integer.parseInt(args[2]);
							
							if( isValidId(currID) && radius <= 100)
								sphere(player, currID, radius, null);

						
							else
								player.sendMessage(ChatColor.RED + "Not a valid item id, or radius > 100!");
						}
						
						catch (NumberFormatException ex)
						{							
							player.sendMessage(ChatColor.RED + "Incorrect values! " + ChatColor.YELLOW + "/replicator sphere [id#] [radius#]");
						}
						
					}
					
					else
						player.sendMessage(ChatColor.RED + "Incorrect syntax. Type in /replicator for help!");

				}//end sphere else if
				
				//Reads from an external file and compiles a figure.
				else if( args[0].equalsIgnoreCase("read") && (sender.isOp() || (permissions == true && permissionHandler.has(player, "replicator.read") )) )
				{
					boolean checker;
					
					if( args.length == 2)
					{
							String file = "plugins/Replicator/" + args[1];
							Scanner outdoors = null;
							try
							{
								outdoors = new Scanner(new File(file));
								checker = true;
							} 
							catch (Exception e) 
							{
								player.sendMessage(ChatColor.RED + "Unexpected exception: File Not Found!");
								checker = false;
							}
						
						if(checker == true)
						{
							outdoors.reset();
							ArrayList<String> userFile = new ArrayList<String>();

							for(int i = 0; outdoors.hasNextLine(); i++)
								userFile.add(outdoors.nextLine());

							ArrayList<String[]> sortedFile = new ArrayList<String[]>();

							for(int i = 0; i < userFile.size(); i++)
								sortedFile.add(userFile.get(i).split(", "));

							Block target = player.getTargetBlock(null, 250);				

							try
							{
								for(int i = 0; i < sortedFile.size(); i++)
									stack(player, target.getX(), target.getY(), target.getZ(), Integer.parseInt(sortedFile.get(i)[0]), Integer.parseInt(sortedFile.get(i)[1]), Integer.parseInt(sortedFile.get(i)[2]), Integer.parseInt(sortedFile.get(i)[3]), 0);
								
								readTarget = target;
								player.sendMessage(ChatColor.DARK_PURPLE + "Your structure has been replicated successfully!");
							}
							
							catch(Exception e)
							{
								player.sendMessage(ChatColor.RED + "Something's wrong with the file you want me to read!");
							}
							
							
						}//end if
						
					}//end creation branch
					
					else if( args.length == 3 && args[2].equals("undo") )
					{
						if(readTarget != null)
						{
							String file = "plugins/Replicator/" + args[1];
							Scanner outdoors = null;
							try
							{
								outdoors = new Scanner(new File(file));
								checker = true;
							} 
							catch (Exception e) 
							{
								player.sendMessage(ChatColor.RED + "Unexpected exception: File Not Found!");
								checker = false;
							}

							if(checker == true)
							{
								outdoors.reset();
								ArrayList<String> userFile = new ArrayList<String>();

								for(int i = 0; outdoors.hasNextLine(); i++)
									userFile.add(outdoors.nextLine());

								ArrayList<String[]> sortedFile = new ArrayList<String[]>();

								for(int i = 0; i < userFile.size(); i++)
									sortedFile.add(userFile.get(i).split(", "));

								try
								{
									for(int i = 0; i < sortedFile.size(); i++)
										stack(player, readTarget.getX(), readTarget.getY(), readTarget.getZ(), 0, Integer.parseInt(sortedFile.get(i)[1]), Integer.parseInt(sortedFile.get(i)[2]), Integer.parseInt(sortedFile.get(i)[3]), 0);
								
									player.sendMessage(ChatColor.BLUE + "Your structure has been removed successfully!");
								}
								
								catch(Exception e)
								{
									player.sendMessage(ChatColor.RED + "Something's wrong with the file you want me to read!");
								}
							}
						}//end readTarget != null
						
						else
							player.sendMessage(ChatColor.RED + "You have to replicate a structure first, dummy!");
						
					}//end undo branch

					else
						player.sendMessage(ChatColor.RED + "Incorrect syntax. Type in /replicator for help!");

				}
				
				else if( args[0].equalsIgnoreCase("disable") && (sender.isOp() || (permissions == true && permissionHandler.has(player, "replicator.disable")) ) )
				{
					player.sendMessage(ChatColor.RED + "You have disabled the Replicator plugin! Reload server to re-enable.");
					player.getServer().getPluginManager().disablePlugin(replicator);
					
				}//end disable else if
				
				else if( args[0].equalsIgnoreCase("help") )
				{
					player.sendMessage(ChatColor.YELLOW + "-----Quick Reference-----");
					player.sendMessage(ChatColor.YELLOW + "This plugin allows for the spontaneous creation of");
					player.sendMessage(ChatColor.YELLOW + "a chosen/avalible design. Using each command");
					player.sendMessage(ChatColor.YELLOW + "correctly can yield powerful results.");
					player.sendMessage(ChatColor.YELLOW + "The \"diamond\" command constructs an elegant diamond,");
					player.sendMessage(ChatColor.YELLOW + "\"cage\" constructs an alright 5x5 cage,");
					player.sendMessage(ChatColor.YELLOW +  "and \"sphere\" creates an elegant sphere!");
					player.sendMessage(ChatColor.GRAY + "More info avalible at: insanj.com/replicator");
					player.sendMessage(ChatColor.RED + "NOTE: Undo works for all objects by deleting. Beware.");
				}
			
				else if( !player.isOp() || (permissions == true && !(permissionHandler.has(player, "replicator.")))  )
					player.sendMessage(ChatColor.DARK_PURPLE + "Sorry! You have to be promoted to use that command! :/");

				else
					player.sendMessage(ChatColor.RED + "Incorrect argument! Need help, " + player.getName() + "? Type in /replicator .");
				
				
				
			}//end diamond branch
							
			else	
			{
				player.sendMessage(ChatColor.RED + "-----Avalible Arguments-----");
				player.sendMessage(ChatColor.YELLOW + "/replicator help");
				player.sendMessage(ChatColor.RED + "/replicator disable");
				player.sendMessage(ChatColor.DARK_AQUA + "/replicator diamond || /replicator diamond [item id]");
				player.sendMessage(ChatColor.GRAY + "/replicator cage || /replicator cage [item id]");
				player.sendMessage(ChatColor.GREEN + "/replicator sphere [item id] [radius]");
				player.sendMessage(ChatColor.GOLD + "/replicator read [filename]");
				player.sendMessage(ChatColor.DARK_PURPLE + "FOR ALL COMMANDS: /[command] undo");
				player.sendMessage("Also, use (/rd || /rc || /rs) for speed!");
				
			}//end exception branch
			
		}//end total command branch
		
		//Runs the quick-command execution method.
		else if( commandLabel.equalsIgnoreCase("rd") && (sender.isOp() || (permissions == true && permissionHandler.has(player, "replicator.diamond")) ))
		{
			if( args.length == 0)
				diamond(player, 5, null);
			
			else if( args.length == 1)
			{
				if( args[0].equals("undo") )
				{
					undoDiamond( (Player) sender );
					return true;
				}
				
				try
				{
					currID = Integer.parseInt(args[0]);
					if( isValidId(currID) )
						diamond(player, currID, null);
					else
						player.sendMessage(ChatColor.RED + "Not a valid item id #!");	
				}
				catch (NumberFormatException ex)
				{							
					player.sendMessage(ChatColor.RED + "The id must be an integer! " + ChatColor.YELLOW + "/rd [id#]");
				}
		
			}//end else if
			
			else
				player.sendMessage(ChatColor.RED + "Incorrect argument! Need help, " + player.getName() + "? Type in /replicator .");
				
		}//end else if	
		
		else if( commandLabel.equalsIgnoreCase("rc") && (sender.isOp() || (permissions == true && permissionHandler.has(player, "replicator.cage")) ))
		{
			if( args.length == 0)
				cage(player, 20, null);
			
			else if( args.length == 1)
			{
				if( args[0].equals("undo") )
				{
					undoCage( (Player) sender );
					return true;
				}
				
				try
				{
					currID = Integer.parseInt(args[0]);
					if( isValidId(currID) )
						cage(player, currID, null);
					else
						player.sendMessage(ChatColor.RED + "Not a valid item id #!");	
				}
				catch (NumberFormatException ex)
				{							
					player.sendMessage(ChatColor.RED + "The id must be an integer! " + ChatColor.YELLOW + "/rc [id#]");
				}
		
			}//end else if
			
			else
				player.sendMessage(ChatColor.RED + "Incorrect argument! Need help, " + player.getName() + "? Type in /replicator .");
				
		}//end else if	
		
		else if( commandLabel.equalsIgnoreCase("rs") && (sender.isOp() || (permissions == true && permissionHandler.has(player, "replicator.sphere")) ) )
		{
			if( args.length == 1)
			{
				if( args[0].equalsIgnoreCase("undo") )
					undoSphere( (Player) sender );
				
				else
					player.sendMessage(ChatColor.RED + "Incorrect syntax. Type in /replicator for help!");
			}
			
			else if (args.length == 2)
			{
				try
				{
					currID = Integer.parseInt(args[0]);
					int radius = Integer.parseInt(args[1]);
					
					if( isValidId(currID) && radius <= 100)
						sphere(player, currID, radius, null);
				
					else
						player.sendMessage(ChatColor.RED + "Not a valid item id, or radius > 100!");
				}
				
				catch (NumberFormatException ex)
				{							
					player.sendMessage(ChatColor.RED + "Incorrect values! " + ChatColor.YELLOW + "/rs [id#] [radius#]");
				}
				
			}
			
			else
				player.sendMessage(ChatColor.RED + "Incorrect syntax. Type in /replicator for help!");

		}//end sphere else if
		
		
		else if( !player.isOp() || (permissions == true && !permissionHandler.has(player, "replicator")) )
			player.sendMessage(ChatColor.DARK_PURPLE + "Sorry! You have to be promoted to use that command! :/");
		
		
		//Because the YAMLYAMLYAML.
		return true;
		
	}//end method onCommand()
	
	//Crudely constructs the diamond.
	public void diamond(Player player, int args, Block helper)
	{
		if( helper == null && (args != 8 && args != 9 && args != 10 && args != 11 ) )
		{
			//Makes the diamond form at the current target of the player.
			//Arguments are (1) the blocks considered transparent, null if only air.
			//(2) The distance the server is willing to take, greater than 100
			Block target = player.getTargetBlock(null, 250);

				//Makes the inner column.
				for(int i = 1; i <= 5; i++)
					stack(player, target.getX(), target.getY(), target.getZ(), args, 0, i, 0, 0);


				//Makes first cross.
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 2, 1, 5);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 2, -1, 6);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 1, 2, 0, 7);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -1, 2, 0, 8);

				//Makes second cross.
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 3, 2, 9);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 3, -2, 10);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 2, 3, 0, 11);		
				stack(player, target.getX(), target.getY(), target.getZ(), args, -2, 3, 0, 12);

				//Second cross filler
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 3, 1, 13);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 3, -1, 14);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 1, 3, 0, 15);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -1, 3, 0, 16);

				//Makes the pretty edges.
				stack(player, target.getX(), target.getY(), target.getZ(), args, 1, 3, 1, 17);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -1, 3, 1, 18);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 1, 3, -1, 19);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -1, 3, -1, 20);

				//Makes third cross.
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 4, 1, 21);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 4, -1, 22);		
				stack(player, target.getX(), target.getY(), target.getZ(), args, 1, 4, 0, 23);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -1, 4, 0, 24);
				
			diamondTarget = target;
			player.sendMessage(ChatColor.DARK_PURPLE + player.getName().toUpperCase() + "'s diamond of ID#" + args + " completed!");
		}//end if
		
		else if( args == 8 || args == 9 || args == 10 || args == 11 )
			player.sendMessage(ChatColor.RED + "You can't make liquid diamonds!");
		
		//Undo!
		else
		{
			//Makes the inner column.
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 1, 0, 0);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 2, 0, 1);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 3, 0, 2);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 4, 0, 3);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 5, 0, 4);

			//Makes first cross.
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 2, 1, 5);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 2, -1, 6);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 1, 2, 0, 7);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -1, 2, 0, 8);

			//Makes second cross.
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 3, 2, 9);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 3, -2, 10);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 2, 3, 0, 11);		
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -2, 3, 0, 12);

			//Second cross filler
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 3, 1, 13);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 3, -1, 14);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 1, 3, 0, 15);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -1, 3, 0, 16);

			//Makes the pretty edges.
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 1, 3, 1, 17);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -1, 3, 1, 18);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 1, 3, -1, 19);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -1, 3, -1, 20);

			//Makes third cross.
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 4, 1, 21);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 4, -1, 22);		
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 1, 4, 0, 23);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -1, 4, 0, 24);
			
			player.sendMessage(ChatColor.BLUE + "Diamond undo successful!");

		}
	}//end method diamond()
	
	//Crudely constructs a cage around current target.
	public void cage(Player player, int args, Block helper)
	{
		//Makes the cage form at the current target of the player.
		//Arguments are (1) the blocks considered transparent, null if only air.
		//(2) The distance the server is willing to take, greater than 100
		Block target = player.getTargetBlock(null, 250);
		
		//Makes the inner column.
		if( helper == null && (args != 8 && args != 9 && args != 10 && args != 11) )
		{	
			for(int i = 1; i <= 5; i++)
			{
				stack(player, target.getX(), target.getY(), target.getZ(), args, 3, i, -2, 0);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 3, i, -1, 1);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 3, i, 0, 2);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 3, i, 1, 3);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 3, i, 2, 4);
			}

			stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 5, 0, 5);
			stack(player, target.getX(), target.getY(), target.getZ(), args, 1, 5, 0, 6);
			stack(player, target.getX(), target.getY(), target.getZ(), args, 2, 5, 0, 7);
			stack(player, target.getX(), target.getY(), target.getZ(), args, -1, 5, 0, 8);
			stack(player, target.getX(), target.getY(), target.getZ(), args, -2, 5, 0, 9);

			stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 5, 0, 10);
			stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 5, 1, 11);
			stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 5, 2, 12);
			stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 5, -1, 13);
			stack(player, target.getX(), target.getY(), target.getZ(), args, 0, 5, -2, 14);


			for(int i = 1; i <= 5; i++)
			{
				stack(player, target.getX(), target.getY(), target.getZ(), args, -3, i, -2, 15);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -3, i, -1, 16);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -3, i, 0, 17);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -3, i, 1, 18);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -3, i, 2, 19);
			}

			for(int i = 1; i <= 5; i++)
			{
				stack(player, target.getX(), target.getY(), target.getZ(), args, -2, i, 3, 20);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -1, i, 3, 21);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, i, 3, 22);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 1, i, 3, 23);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 2, i, 3, 24);
			}

			for(int i = 1; i <= 5; i++)
			{
				stack(player, target.getX(), target.getY(), target.getZ(), args, -2, i, -3, 25);
				stack(player, target.getX(), target.getY(), target.getZ(), args, -1, i, -3, 26);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 0, i, -3, 27);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 1, i, -3, 28);
				stack(player, target.getX(), target.getY(), target.getZ(), args, 2, i, -3, 29);
			}

			cageTarget = target;
			player.sendMessage(ChatColor.DARK_PURPLE + player.getName().toUpperCase()  + "'s cage of ID#" + args + " completed!");
		}
		
		else if( args == 8 || args == 9 || args == 10 || args == 11 )
			player.sendMessage(ChatColor.RED + "You can't make liquid cages!");
		
		else
		{	
			for(int i = 1; i <= 5; i++)
			{
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 3, i, -2, 0);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 3, i, -1, 1);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 3, i, 0, 2);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 3, i, 1, 3);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 3, i, 2, 4);
			}

			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 5, 0, 5);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 1, 5, 0, 6);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 2, 5, 0, 7);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -1, 5, 0, 8);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -2, 5, 0, 9);

			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 5, 0, 10);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 5, 1, 11);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 5, 2, 12);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 5, -1, 13);
			stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, 5, -2, 14);


			for(int i = 1; i <= 5; i++)
			{
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -3, i, -2, 15);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -3, i, -1, 16);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -3, i, 0, 17);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -3, i, 1, 18);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -3, i, 2, 19);
			}

			for(int i = 1; i <= 5; i++)
			{
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -2, i, 3, 20);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -1, i, 3, 21);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, i, 3, 22);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 1, i, 3, 23);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 2, i, 3, 24);
			}

			for(int i = 1; i <= 5; i++)
			{
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -2, i, -3, 25);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, -1, i, -3, 26);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 0, i, -3, 27);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 1, i, -3, 28);
				stack(player, helper.getX(), helper.getY(), helper.getZ(), args, 2, i, -3, 29);
			}

			player.sendMessage(ChatColor.BLUE + "Cage undo successful!");
		}
		
	}//end method cage()
	
	//Constructs the most elegant sphere at your target.
	public void sphere(Player player, int args, int radius, Block helper)
	{
		Block target = player.getTargetBlock(null, 250);
		
		//regular sphere
		if( args != 8 && args != 9 && args != 10 && args != 11 && args != 0)
		{
			if( helper == null )
			{	
				int x0 = target.getX();
				int y0 = target.getY();
				int z0 = target.getZ();
				for(int theta = 0; theta < 180; ++theta) {
					for(int phi = 0; phi < 360; ++phi) {
						double x = x0 + radius*Math.sin(Math.toRadians(theta))*Math.cos(Math.toRadians(phi));
						double y = y0 + radius*Math.sin(Math.toRadians(theta))*Math.sin(Math.toRadians(phi));
						double z = z0 + radius*Math.cos(Math.toRadians(theta));

						stackSphere(player, (int)x, (int)y, (int)z, args);
					}
				}

				sphereTarget = target;
				sphereRadius = radius;
				player.sendMessage(ChatColor.DARK_PURPLE + player.getName().toUpperCase()  + "'s sphere of ID#" + args + " completed!");

			}//end if
			
		}//end regular sphere
		
		//For liquid containing spheres.
		else
		{
			if( helper == null && args == 8 || args == 9 || args == 10 || args == 11)
			{
				int x0 = target.getX();
				int y0 = target.getY();
				int z0 = target.getZ();
				for(int theta = 0; theta < 180; ++theta) {
					for(int phi = 0; phi < 360; ++phi) {
						double x = x0 + radius*Math.sin(Math.toRadians(theta))*Math.cos(Math.toRadians(phi));
						double y = y0 + radius*Math.sin(Math.toRadians(theta))*Math.sin(Math.toRadians(phi));
						double z = z0 + radius*Math.cos(Math.toRadians(theta));

						stackSphere(player, (int)x, (int)y, (int)z, 20);
					}
				}
						
						
						for(int theta = 0; theta < 180; ++theta) {
							for(int phi = 0; phi < 360; ++phi) {
								double x = x0 + (radius-1)*Math.sin(Math.toRadians(theta))*Math.cos(Math.toRadians(phi));
								double y = y0 + (radius-1)*Math.sin(Math.toRadians(theta))*Math.sin(Math.toRadians(phi));
								double z = z0 + (radius-1)*Math.cos(Math.toRadians(theta));

								stackSphere(player, (int)x, (int)y, (int)z, args);
							}
						}
						
				sphereTarget = target;
				sphereRadius = radius;
				player.sendMessage(ChatColor.DARK_PURPLE + player.getName().toUpperCase()  + "'s" + ChatColor.AQUA + " LIQUID " + ChatColor.DARK_PURPLE + "sphere of ID#" + args + " completed!");

			
			}//end if

					
			else if( helper == null && args != 8 && args != 9 && args != 10 && args != 11 )
			{	
				int x0 = target.getX();
				int y0 = target.getY();
				int z0 = target.getZ();
				for(int theta = 0; theta < 180; ++theta) {
					for(int phi = 0; phi < 360; ++phi) {
						double x = x0 + radius*Math.sin(Math.toRadians(theta))*Math.cos(Math.toRadians(phi));
						double y = y0 + radius*Math.sin(Math.toRadians(theta))*Math.sin(Math.toRadians(phi));
						double z = z0 + radius*Math.cos(Math.toRadians(theta));

						stackSphere(player, (int)x, (int)y, (int)z, args);
					}
				}

				sphereTarget = target;
				sphereRadius = radius;
				player.sendMessage(ChatColor.DARK_PURPLE + player.getName().toUpperCase()  + "'s sphere of ID#" + args + " completed!");

			}//end else if
			
			
			//UNDO!
			else
			{	
				int x0 = helper.getX();
				int y0 = helper.getY();
				int z0 = helper.getZ();
				for(int theta = 0; theta < 180; ++theta) {
					for(int phi = 0; phi < 360; ++phi) {
						double x = x0 + radius*Math.sin(Math.toRadians(theta))*Math.cos(Math.toRadians(phi));
						double y = y0 + radius*Math.sin(Math.toRadians(theta))*Math.sin(Math.toRadians(phi));
						double z = z0 + radius*Math.cos(Math.toRadians(theta));

						stackSphere(player, (int)x, (int)y, (int)z, args);			
					}
				}

				for(int theta = 0; theta < 90; ++theta) {
					for(int phi = 0; phi < 180; ++phi) {
						double x = x0 + radius*Math.sin(Math.toRadians(theta))*Math.cos(Math.toRadians(phi));
						double y = y0 + radius*Math.sin(Math.toRadians(theta))*Math.sin(Math.toRadians(phi));
						double z = z0 + radius*Math.cos(Math.toRadians(theta));

						stackSphere(player, (int)x, (int)y, (int)z, args);			
					}
				}

				player.sendMessage(ChatColor.BLUE + "Sphere undo successful!");

			}//end else
		}
				
		
	}//end method sphere()
	
	public void stack(Player player, int x, int y, int z, int id, int additiveX, int additiveY, int additiveZ, int loc)
	{
		Location temp = new Location( player.getWorld(), (x + additiveX), (y + additiveY), (z + additiveZ) );
		Block block = player.getWorld().getBlockAt(temp);
		block.setTypeId(id);
	}//end method stack()
	
	
	public void stackSphere(Player player, int x, int y, int z, int id)
	{
		Location temp = new Location( player.getWorld(), x, y, z);
		Block block = player.getWorld().getBlockAt(temp);
		block.setTypeId(id);
	}//end method stacksphere()
	
	public void undoDiamond(Player player)
	{
		diamond( player, 0, diamondTarget );
	}
	
	public void undoCage(Player player)
	{
		cage( player, 0, cageTarget );
	}
	
	public void undoSphere(Player player)
	{
		sphere( player, 0, sphereRadius, sphereTarget);
	}
			  
}//end class replicator


/***********************************Contents of "plugin.yml":*******************************
name: Replicator
version: 1.4 ["TANGENT"]
author: insanj
main: me.insanj.replicator.replicator
description: Create, destroy, be stupid.
website: http://www.insanj.com/replicator

commands:
  replicator:
    #Permissions is only for the disable command, the rest is useable by anyone.
    permissions: replicator.disable
    description: Lists available arguments for the plugin, or disables.
    usage: |
          /<command>
          /<command> disable
          /<commnad> help

  replicator diamond:
    description: Creates a beautiful diamond out of a material (wood by default).
    permissions: replicator.diamond
    usage: |
          /<command>
          /<command> [item id#]
          /<command> undo

  rd:
    description: Quick way to create a diamond.
    permissions: replicator.diamond
    usage: |
          /<command>
          /<command> [item id]
          /<command> undo

  replicator cage:
    description: Creates a cage of an item id or class.
    permissions: replicator.cage
    usage: |
          /<command>
          /<command> [item id]
          /<command> undo

  rc:
    description: Quick way to create a cage.
    permissions: replicator.cage
    usage: |
          /<command>
          /<command> [item id]
          /<command> undo

  replicator sphere:
    description: Create the most incredible sphere.
    permissions: replicator.sphere
    usage: |
          /<command> [item id] [radius]
          /<command> undo

  rs:
    description: Quick way to create a sphere.
    permissions: replicator.sphere
    usage: |
          /<command> [item id] [radius]
          /<command> undo

  replicator read:
    permissions: replicator.read
    description: Create your own objects! (More info in changelog)
    usage: |
          /<command> [file_name_in_current_directory.txt]
          /<command> [file_name_in_current_directory.txt] undo

******************************************************************************************/

/**************************************Changelog and Bugs/README***************************
- means feature or issue removed.
+ means feature added.
* means bug of feature fixed.

BUGS/TODO:
+Undo works only by deleting/replacing diamonds (with air), and no cages.

CHANGELOG:

1.0
*Created first plugin, detects portal placement.
+When portal is placed, finally, it is changed into a wood block, and a neat structure takes its place.
+Made complex structure branch off, with partitions.
+Structure finally traps player, almost makes it impossible NOT to die.
*Fixed all bugs associated, especially the infinite loop.

1.1
-Scrapped killing player/branched idea, and worked on diamond.
*Placing portal will AT LAST create a weird looking diamond out of wood.
+The diamond is jump-insidable (by accident)!
+FIRST PLUGIN OFFICIAL RELEASE!

1.2
+Added commands! No more stupid portals!
*The "plugin.yml" has a use, and can be easily read and understood.
+Arguments and Quickcommands added! Make diamonds out of glass, use only four total keystrokes!
+Added help menus and a Quick Reference, and a more organized if/else branch system.
*Made the diamonds spawn one block up from current block, waaaay better.

1.2_01
*Diamond creation more streamlined, only "valid" blocks spawn, no more apparent bugs.

1.2_02
+Cages added (on request from poolshark)! At your current target, a sloppy about 5x5ish box is made!

1.2_03
+Added, after hours of tedious work, an undo function, and streamlined code more. Determined the only way to implement undo as of now is to make the undo replace the last Diamond placed with air. But, the "last diamond placed" thingy is still neat, in my books.
+Released changelog/etc online, and made a mental note to create way more structures ASAP, focus more on video-making next Thursday, and have undo be a necessity. 

1.3
*Cleaned up code, again, and added OP support. Permissions coming next update, but sending off to Bukkit Plugin List now!

1.4
*Finally got approved on the Bukkit list! Cool!
+With much help from my brother, added incredibly perfect spheres (just 9 lines of code)! You get to choose your radius, too.
+Added a  "disable" feature, so I can disable the plugin easily, even though I still can't update seamlessly! >:(
*Fixed a wall of the cage spawning one block lower than the rest.
*Doesn't allow undos without a structure creation first. (Almost overlooked this one!)
+Added an awesome feature where the user can create his/her own unique structures from an external file! [Ω]
+Added support of the all-loving Permissions plugin! [∆]
-Got rid of cages being made of liquids, as well as diamonds.
*Modified the general layout of the plugin.yml, adding the nodes for Permissions (for fun!).
+Generates a folder on first run, and then stores a temporary "readme" file.
+Added fancy brackets around the log info messages!
*Fixed a bunch of misc. errors, making sure that the plugin handles everything, and no "Internal errors" pop up. Hopefully that's all, but I'm relying on a few forum-testers to prove me wrong!


Ω - By creating a readable, .txt, file in the Replicator-generated directory, you can create your own structures! Every line will be a new block placement, with the following format: 
		[item id], [x additive], [y additive], [z additive]
Where the additives are the additions to the current block you're looking at. So, to make a three-block high column of wood…
		5, 0, 1, 0
		5, 0, 2, 0
		5, 0, 3, 0

∆ - If you have the Permissions plugin enabled, then the nodes are:
		replicator.diamond
		replicator.cage
		replicator.sphere
		replicator.read
		replicator.disable
    Without the Permissions plugin enabled, Replicator will default to OP.
******************************************************************************************/


