package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MagicalBallOfYarnItem extends Item implements DyeableItem {

	public MagicalBallOfYarnItem(Settings item$Settings_1) {
		super(item$Settings_1);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world_1, List<Text> list, TooltipContext tooltipContext_1) {
		if(stack.hasNbt() && stack.getNbt().contains("origin")) {
			NbtCompound origin = stack.getSubNbt("origin");
			list.add(Text.translatable("soulmagic.magical_ball_of_yarn.text", origin.getInt("x"), origin.getInt("y"), origin.getInt("z")));
		}
	}
	
	@Override
	public int getColor(ItemStack itemStack_1) {
		NbtCompound compoundTag_1 = itemStack_1.getSubNbt("display");
		return compoundTag_1 != null && compoundTag_1.contains("color", 99) ? compoundTag_1.getInt("color") : 0xFFFFFF;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		
		if(selected && world.isClient && stack.hasNbt() && stack.getNbt().contains("path")) {
			NbtList tag = (NbtList) stack.getNbt().get("path");
			NbtCompound tag2 = null;
			int color = this.getColor(stack);
			
			DustParticleEffect particle = new DustParticleEffect(new Vector3f((color >>> 16) / 255f, (color >>> 8 & 255) / 255f, (color & 255) / 255f), 1);
			
			
			for(int i = 0; i < tag.size(); i++) {
				tag2 = (NbtCompound) tag.get(i);
				
				world.addParticle(particle, tag2.getInt("x") + 0.5,  tag2.getInt("y") + 0.5,  tag2.getInt("z") + 0.5, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		
		if(player.isSneaking()) {
			NbtCompound tag = stack.getOrCreateSubNbt("origin");
			tag.putInt("x", (int) player.getX());
			tag.putInt("y", (int) player.getY());
			tag.putInt("z", (int) player.getZ());
			
			if(!world.isClient) {
				player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F, (world.random.nextFloat() - world.random.nextFloat()) * 0.35F + 0.9F);
				player.sendMessage(Text.translatable("soulmagic.magical_ball_of_yarn.text", (int) player.getX(), (int) player.getY(), (int) player.getZ()), true);
			}
		} else {
			Thread t = new Thread(() ->  {
				if(!world.isClient && stack.hasNbt() && stack.getNbt().contains("origin")) {
					
					NbtCompound tag = stack.getSubNbt("origin");
					
					Node startNode = new Node((int) player.getX(), (int) player.getY(), (int) player.getZ(), null, 0);
					Node goalNode = new Node(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"), null, 0);
					
					startNode.f = this.getHueristic(startNode, goalNode);
					
					List<Node> OPEN = new ArrayList<>();
					List<Node> CLOSED = new ArrayList<>();
					
					OPEN.add(startNode);
					
					//if(isGoal(pos, pos2)) {
					//	return TypedActionResult.success(player.getStackInHand(hand));
					//}
					
					while(!OPEN.isEmpty()) {
						Node q = OPEN.get(0);
						
						for(Node e : OPEN) {
							if(e.f < q.f) {
								q = e;
							}
						}
						
						OPEN.remove(q);
						
						MAIN: for(Node succNode : q.getSuccessors(player, world)) {
							//System.out.println("ddddddddddd");
							if(succNode.equals(goalNode)) {
								Node pnode = succNode;
								NbtList list = new NbtList();
								
								//System.out.println("------------------------------------------");
								while(pnode != null) {
									NbtCompound posTag = new NbtCompound();
									posTag.putInt("x", pnode.x);
									posTag.putInt("y", pnode.y);
									posTag.putInt("z", pnode.z);
									
									list.add(posTag);
									//MinecraftClient.getInstance().worldRenderer.render(matrixStack_1, float_1, long_1, boolean_1, camera_1, gameRenderer_1, lightmapTextureManager_1, matrix4f_1);
									//System.out.println(pnode.pos);
									pnode = pnode.parent;
								}
								stack.getNbt().put("path", list);
								//return TypedActionResult.success(player.getStackInHand(hand));
								return;
							}
							
							succNode.g = succNode.g + q.g;
							succNode.f = succNode.g + this.getHueristic(succNode, goalNode);
							
							for(Node e : OPEN) {
								if(e.equals(succNode) && e.f <= succNode.f) {
									continue MAIN;
								}
							}
							
							for(Node e : CLOSED) {
								if(e.equals(succNode) && e.f <= succNode.f) {
									continue MAIN;
								}
							}
							
							OPEN.add(succNode);
						}
						
						CLOSED.add(q);
						//System.out.println(CLOSED.size());
					}
				}
				
				/*if(!world.isClient && stack.hasTag() && stack.getTag().contains("x") && stack.getTag().contains("y") && stack.getTag().contains("z")) {
					System.out.println("Starting calculation");
					CompoundTag tag = stack.getTag();
					
					BlockPos startNode = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ());
					BlockPos goalNode = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
					Map<BlockPos, Integer> gMap = new HashMap<>();
					Map<BlockPos, Double> fMap = new HashMap<>();
					
					gMap.put(startNode, 0);
					fMap.put(startNode, this.getHueristic(startNode, goalNode));
					
					List<BlockPos> OPEN = new ArrayList<BlockPos>();
					List<BlockPos> CLOSED = new ArrayList<BlockPos>();
					
					OPEN.add(startNode);
					
					//if(isGoal(pos, pos2)) {
					//	return TypedActionResult.success(player.getStackInHand(hand));
					//}
					
					while(!OPEN.isEmpty()) {
						BlockPos q = OPEN.get(0);
						
						for(BlockPos e : OPEN) {
							if(fMap.get(e) < fMap.get(q)) {
								q = e;
							}
						}
						
						OPEN.remove(q);
						
						int g = gMap.get(q);
						MAIN: for(BlockPos succNode : getSuccessors(world, q)) {
							//System.out.println(succNode.pos);
							if(succNode.equals(goalNode)) {
								//BlockPos pnode = succNode;
								System.out.println("------------------------------------------");
								//while(pnode.parent != null) {
								//	world.setBlockState(pnode.pos, ModBlocks.SOUL_FLAME.getDefaultState());
								//	//System.out.println(pnode.pos);
								//	pnode = pnode.parent;
								//}
														
								//return TypedActionResult.success(player.getStackInHand(hand));
								return;
							}
							
							gMap.put(succNode, g + 1);
							fMap.put(succNode, g + this.getHueristic(succNode, goalNode));
							
							for(BlockPos e : OPEN) {
								if(e.equals(succNode) && fMap.get(e) < fMap.get(succNode)) {
									continue MAIN;
								}
							}
							
							for(BlockPos e : CLOSED) {
								if(e.equals(succNode) && fMap.get(e) < fMap.get(succNode)) {
									continue MAIN;
								}
							}
							
							OPEN.add(succNode);
						}
						
						CLOSED.add(q);
					}
				}*/
				
				/*if(!world.isClient && stack.hasTag() && stack.getTag().contains("x") && stack.getTag().contains("y") && stack.getTag().contains("z")) {
					System.out.println("Starting calculation");
					CompoundTag tag = stack.getTag();
					
					BlockPos startNode = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ());
					BlockPos goalNode = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
					Map<BlockPos, Integer> gMap = new HashMap<>();
					Map<BlockPos, Double> fMap = new HashMap<>();
					
					gMap.put(startNode, 0);
					fMap.put(startNode, this.getHueristic(startNode, goalNode));
					
					List<BlockPos> OPEN = new ArrayList<BlockPos>();
					List<BlockPos> CLOSED = new ArrayList<BlockPos>();
					
					OPEN.add(startNode);
					
					if(startNode.equals(goalNode)) {
						System.out.println("------------------------------------");
						return;
					}
					
					while(!OPEN.isEmpty()) {
						BlockPos q = OPEN.get(0);
						
						for(BlockPos e : OPEN) {
							if(fMap.get(e) < fMap.get(q)) {
								q = e;
							}
						}
						
						OPEN.remove(q);
						
						int g = gMap.get(q);
						MAIN: for(BlockPos succNode : getSuccessors(world, q)) {
							//System.out.println(succNode.pos);
							if(succNode.equals(goalNode)) {
								//BlockPos pnode = succNode;
								System.out.println("------------------------------------------");
								//while(pnode.parent != null) {
								//	world.setBlockState(pnode.pos, ModBlocks.SOUL_FLAME.getDefaultState());
								//	//System.out.println(pnode.pos);
								//	pnode = pnode.parent;
								//}
														
								//return TypedActionResult.success(player.getStackInHand(hand));
								return;
							}
							
							gMap.put(succNode, g + 1);
							fMap.put(succNode, g + this.getHueristic(succNode, goalNode));
							
							for(BlockPos e : OPEN) {
								if(e.equals(succNode) && fMap.get(e) < fMap.get(succNode)) {
									continue MAIN;
								}
							}
							
							for(BlockPos e : CLOSED) {
								if(e.equals(succNode) && fMap.get(e) < fMap.get(succNode)) {
									continue MAIN;
								}
							}
							
							OPEN.add(succNode);
						}
						
						CLOSED.add(q);
					}
				}*/
			});
			
			t.start();
		}
		return TypedActionResult.pass(player.getStackInHand(hand));
	}
	
	public double getHueristic(Node n1, Node n2) {
		//double c = Math.sqrt(Math.pow(n1.x - n2.x, 2) + Math.pow(n1.y - n2.y, 2) + Math.pow(n1.z - n2.z, 2));
		//System.out.println(c);
		//return c;
		return Math.abs(n1.x - n2.x) + Math.abs(n1.y - n2.y) + Math.abs(n1.z - n2.z);
		//return Math.max(Math.abs(pos1.getX() - pos2.getX()), Math.max(Math.abs(pos1.getY() - pos2.getY()), Math.abs(pos1.getZ() - pos2.getZ())));
	}
	
	/*private List<BlockPos> getSuccessors(World world, BlockPos parent) {
		List<BlockPos> successors = new ArrayList<BlockPos>();
		//TODO Add up and down
		
		if(world.getBlockState(parent).isAir()) {
			successors.add(parent.north());
			world.setBlockState(parent.north(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(parent.south());
			world.setBlockState(parent.south(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(parent.east());
			world.setBlockState(parent.east(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(parent.west());
			world.setBlockState(parent.west(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(parent.up());
			world.setBlockState(parent.up(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(parent.down());
			world.setBlockState(parent.down(), ModBlocks.SOUL_FLAME.getDefaultState());
		}
		return successors;
	}*/

	/*private List<BlockPosNode> getSuccessors(World world, BlockPosNode parent) {
		List<BlockPosNode> successors = new ArrayList<BlockPosNode>();
		//TODO Add up and down
		
		if(world.getBlockState(parent.pos).isAir() || world.getBlockState(parent.pos).getBlock() == ModBlocks.SOUL_FLAME) {
			successors.add(new BlockPosNode(parent.pos.north(), parent));
			world.setBlockState(parent.pos.north(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(new BlockPosNode(parent.pos.south(), parent));
			world.setBlockState(parent.pos.south(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(new BlockPosNode(parent.pos.east(), parent));
			world.setBlockState(parent.pos.east(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(new BlockPosNode(parent.pos.west(), parent));
			world.setBlockState(parent.pos.west(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(new BlockPosNode(parent.pos.up(), parent));
			world.setBlockState(parent.pos.up(), ModBlocks.SOUL_FLAME.getDefaultState());
			successors.add(new BlockPosNode(parent.pos.down(), parent));
			world.setBlockState(parent.pos.down(), ModBlocks.SOUL_FLAME.getDefaultState());
		}
		return successors;
	}*/

	//pos1 is player pos, and pos2 is the goal
	//public int getHueristic(Node node1, Node node2) {
	//	BlockPos pos1 = node1.pos;
	//	BlockPos pos2 = node2.pos;
		
	///	return Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getY() - pos2.getY()) + Math.abs(pos1.getZ() - pos2.getZ());
	//}
	
	public static class Node {
		
		private int x, y, z;
		
		private Node parent;
		
		private double f;
		private int g;
		
		public Node(int x, int y, int z, Node parent, int g) {
			this.x = x;
			this.y = y;
			this.z = z;
			
			this.parent = parent;
			this.g = g;
		}
		
		public List<Node> getSuccessors(PlayerEntity player, World world) {
			List<Node> successors = new ArrayList<Node>();
			BlockPos pos = new BlockPos(this.x, this.y, this.z);
			BlockState state = world.getBlockState(pos);
			if(state.isAir() || state.getCollisionShape(world, pos) != VoxelShapes.fullCube()) {
				for(int x = -1; x <= 1; x++) {
					for(int y = -1; y <= 1; y++) {
						for(int z = -1; z <= 1; z++) {
							if(x != 0 || y != 0 || z != 0) {
								int step = /*!player.canFly() && */world.getBlockState(new BlockPos(this.x + x, this.y + y - 1, this.z + z)).isAir() ? 5 : 1;
								successors.add(new Node(this.x + x, this.y + y, this.z + z, this, step));
							}
						}
					}
				}
			}
			
			return successors;
		}
		
		private void placeBlock(World world, int x, int y, int z) {
			world.setBlockState(new BlockPos(x, y, z), ModBlocks.SOUL_FLAME.getDefaultState());
		}

		@Override
		public boolean equals(Object o) {
			Node n = (Node) o;
			return this.x == n.x && this.y == n.y && this.z == n.z;
		}
	}
}
