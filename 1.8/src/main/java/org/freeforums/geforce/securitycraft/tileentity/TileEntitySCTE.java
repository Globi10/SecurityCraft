package org.freeforums.geforce.securitycraft.tileentity;

import java.util.Iterator;
import java.util.List;

import org.freeforums.geforce.securitycraft.api.IIntersectable;
import org.freeforums.geforce.securitycraft.api.IViewActivated;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class TileEntitySCTE extends TileEntity implements IUpdatePlayerListBox{

	protected boolean intersectsEntities = false;
	protected boolean viewActivated = false;

	public void update() {
		if(intersectsEntities){
			int i = this.pos.getX();
	        int j = this.pos.getY();
	        int k = this.pos.getZ();
	        AxisAlignedBB axisalignedbb = (new AxisAlignedBB((double)i, (double)j, (double)k, (double)(i + 1), (double)(j + 1), (double)(k + 1)));
	        List list = this.worldObj.getEntitiesWithinAABB(Entity.class, axisalignedbb);
	        Iterator iterator = list.iterator();
	        Entity entity;
	
	        while (iterator.hasNext())
	        {
	        	entity = (Entity)iterator.next();
	        	entityIntersecting(entity);
	        }	        	   
		}
		
		if(viewActivated){
			int i = this.pos.getX();
	        int j = this.pos.getY();
	        int k = this.pos.getZ();
	        AxisAlignedBB axisalignedbb = (new AxisAlignedBB((double)i, (double)j, (double)k, (double)(i), (double)(j), (double)(j)).expand(5, 5, 5));
	        List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
	        Iterator iterator = list.iterator();
	        EntityPlayer entityPlayer;
	
	        while (iterator.hasNext())
	        {
	        	entityPlayer = (EntityPlayer)iterator.next();
	        	double eyeHeight = (double) entityPlayer.getEyeHeight();
	        	
	        	Vec3 lookVec = new Vec3((entityPlayer.posX + (entityPlayer.getLookVec().xCoord * 5)), ((eyeHeight + entityPlayer.posY) + (entityPlayer.getLookVec().yCoord * 5)), (entityPlayer.posZ + (entityPlayer.getLookVec().zCoord * 5)));
	        	
	        	MovingObjectPosition mop = getWorld().rayTraceBlocks(new Vec3(entityPlayer.posX, entityPlayer.posY + entityPlayer.getEyeHeight(), entityPlayer.posZ), lookVec);
	        	if(mop != null && mop.typeOfHit == MovingObjectType.BLOCK){
	        		if(mop.getBlockPos().getX() == getPos().getX() && mop.getBlockPos().getY() == getPos().getY() && mop.getBlockPos().getZ() == getPos().getZ()){
	    	        	activatedByView(entityPlayer);
	        		}
	        	}
	        }
		}
	}
	
	public void entityIntersecting(Entity entity) {
		((IIntersectable) this.worldObj.getBlockState(getPos()).getBlock()).onEntityIntersected(getWorld(), getPos(), entity);
	}
	
	public void activatedByView(EntityPlayer entity) {
		((IViewActivated) this.worldObj.getBlockState(getPos()).getBlock()).onEntityLookedAtBlock(getWorld(), getPos(), entity);
	}
	
	/**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	
        par1NBTTagCompound.setBoolean("intersectsEntities", intersectsEntities);
        par1NBTTagCompound.setBoolean("viewActivated", viewActivated);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("intersectsEntities"))
        {
            this.intersectsEntities = par1NBTTagCompound.getBoolean("intersectsEntities");
        }
        
        if (par1NBTTagCompound.hasKey("viewActivated"))
        {
            this.viewActivated = par1NBTTagCompound.getBoolean("viewActivated");
        }
    }
    
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState){
    	return (oldState.getBlock() != newState.getBlock());
    }
    
    /**
     * Sets the TileEntity able to be intersected with. 
     * <p>
     * Calls {@link IIntersectable}.onEntityIntersected(World, BlockPos, EntityLivingBase) when a {@link EntityLivingBase} comes into contact with this block.
     * <p>
     * Implement IIntersectable in your Block class in order to do stuff with that event.
     */
    public TileEntitySCTE intersectsEntities(){
        intersectsEntities = true;
        return this;
    }
    
    /**
     * Sets the TileEntity able to be activated when a player looks at the block. 
     * <p>
     * Calls {@link IViewActivated}.onEntityLookedAtBlock(World, BlockPos, EntityLivingBase) when a {@link EntityPlayer} looks at this block.
     * <p>
     * Implement IViewActivated in your Block class in order to do stuff with that event.
     */
    public TileEntitySCTE viewActivated(){
        viewActivated = true;
        return this;
    }

}
