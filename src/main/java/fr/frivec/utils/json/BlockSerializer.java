package fr.frivec.utils.json;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class BlockSerializer extends TypeAdapter<Block> {

	@Override
	public Block read(JsonReader reader) throws IOException {
		
		int x = 0, y = 0, z = 0;
		Material material = null;
		String worldName = "world";
		
		reader.beginObject();
		
		while(reader.hasNext()) {
		
			switch (reader.nextName()) {
			
			case "x":
				
				x = reader.nextInt();
				
				break;
				
			case "y":
				
				y = reader.nextInt();
				
				break;
				
			case "z":
				
				z = reader.nextInt();
				
				break;
				
			case "material":
				
				material = Material.getMaterial(reader.nextString());
				
				break;
				
			case "world":
				
				worldName = reader.nextString();
				
				break;
	
			default:
				break;
			}
			
		}
		
		reader.endObject();
		
		final Block block = Bukkit.getWorld(worldName).getBlockAt(x, y, z);
		
		block.setType(material);
		
		return block;
	}

	@Override
	public void write(JsonWriter writer, Block block) throws IOException {
		
		writer.beginObject();
		
		writer.name("x").value(block.getX());
		writer.name("y").value(block.getY());
		writer.name("z").value(block.getZ());
		writer.name("material").value(block.getType().name());
		writer.name("world").value(block.getLocation().getWorld().getName());
		
		writer.endObject();
		
	}

}
