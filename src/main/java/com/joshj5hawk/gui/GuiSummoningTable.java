package com.joshj5hawk.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.joshj5hawk.container.ContainerSummoningTable;
import com.joshj5hawk.crafting.SummoningRecipes;
import com.joshj5hawk.item.ItemSummoningBook;
import com.joshj5hawk.lib.Strings;
import com.joshj5hawk.tileentity.TileEntitySummoningTable;

public class GuiSummoningTable extends GuiContainer
{

    private ResourceLocation texutre = new ResourceLocation(Strings.modid + ":" + "textures/gui/summoningTable.png");
    private TileEntitySummoningTable summoningTable;
    private float rot;

    public GuiSummoningTable(InventoryPlayer invPlayer, TileEntitySummoningTable tileEntityST)
    {
        super(new ContainerSummoningTable(invPlayer, tileEntityST));
        summoningTable = tileEntityST;

        this.xSize = 176;
        this.ySize = 166;
    }

    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
    	EnumChatFormatting color = EnumChatFormatting.GREEN;
        String name = this.summoningTable.hasCustomInventoryName() ? this.summoningTable.getInventoryName() : I18n.format(this.summoningTable.getInventoryName(),
                new Object[0]);
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) - 70 / 2, 6, 4210752);
        this.fontRendererObj.drawString("Table", this.xSize / 2 - this.fontRendererObj.getStringWidth("Table") - 95 / 2, 15, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 63, this.ySize - 96 + 2, 4210752);

        ItemStack stack1 = summoningTable.getStackInSlot(0);
        ItemStack stack2 = summoningTable.getStackInSlot(1);
        if (stack1 != null && stack2 != null)
        {
            renderEntity(stack1, stack2);
        }
        if(summoningTable.dualFuel / 100 >= 16)
        {
        	color = EnumChatFormatting.GREEN;
        }
        else if(summoningTable.dualFuel / 100 >=6)
        {
        	color = EnumChatFormatting.YELLOW;
        }
        else
        {
        	color = EnumChatFormatting.RED;
        }
        int w = (this.width - this.xSize) / 2;
        int h = (this.height - this.ySize) / 2;
        List<String> ttLines = new ArrayList<String>();
        if (summoningTable != null && mouseX < w + 66 && mouseX > w + 52 && mouseY > h + 6 && mouseY < h + 38)
        {
            ttLines.add(color + "" + summoningTable.dualFuel / 200 + EnumChatFormatting.WHITE + " Summons Remain");
            this.func_146283_a(ttLines, mouseX - w, mouseY - h);
        }
    }

    // mostly copied from bspkrs core, do not touch
    private void renderEntity(ItemStack stack1, ItemStack stack2)
    {
        float posX = 135;
        float posY = 35;
        
        ItemStack result = SummoningRecipes.INSTANCE.getSummoningResult(stack1, stack2);
        if(result != null)
        {
        	EntityLiving ent = (EntityLiving) EntityList.createEntityByName(ItemSummoningBook.getSimpleEntityName(ItemSummoningBook.getTag(result).getString(ItemSummoningBook.ENTITY_KEY)), mc.theWorld);
        	GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        	GL11.glDisable(GL11.GL_BLEND);
        	GL11.glDepthMask(true);
        	GL11.glEnable(GL11.GL_DEPTH_TEST);
        	GL11.glEnable(GL11.GL_ALPHA_TEST);
        	GL11.glPushMatrix();
        	GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        	GL11.glTranslatef(posX, posY, 50.0F);
        	GL11.glScalef((-25), 25, 25);
        	GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        	GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        	GL11.glRotatef(rot, 0, 1, 0);
        	RenderHelper.enableStandardItemLighting();
        	GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        	ent.rotationYawHead = ent.renderYawOffset;
        	GL11.glTranslatef(0.0F, -ent.height / 2, 0.0F);
        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	RenderManager.instance.playerViewY = 180.0F;
        	RenderManager.instance.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        	GL11.glPopMatrix();
        	RenderHelper.disableStandardItemLighting();
        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        	OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        	GL11.glDisable(GL11.GL_TEXTURE_2D);
        	OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        	GL11.glTranslatef(0.0F, 0.0F, 20F);
        	GL11.glPopAttrib();
        	rot += 0.5f;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texutre);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (summoningTable.hasFuel())
        {
            int i1 = summoningTable.getFuelRemainingScaled(30);
            drawTexturedModalRect(guiLeft + 54, guiTop + 37 - i1, 227, 31 - i1, 12, i1);
        }

        int j1 = summoningTable.getSummoningProgressScaled(72);
        drawTexturedModalRect(guiLeft + 43, guiTop + 36, 176, 33, j1 + 1, 43);
    }
}
