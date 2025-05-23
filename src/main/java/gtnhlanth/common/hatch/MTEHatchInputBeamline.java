package gtnhlanth.common.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.IConnectsToBeamline;

public class MTEHatchInputBeamline extends MTEHatchBeamlineConnector {

    private boolean delay = true;

    private static final String activeIconPath = "iconsets/OVERLAY_BI_ACTIVE";
    private static final String sideIconPath = "iconsets/OVERLAY_BI_SIDES";
    private static final String connIconPath = "iconsets/BI_CONN";

    private static final Textures.BlockIcons.CustomIcon activeIcon = new Textures.BlockIcons.CustomIcon(activeIconPath);
    private static final Textures.BlockIcons.CustomIcon sideIcon = new Textures.BlockIcons.CustomIcon(sideIconPath);
    private static final Textures.BlockIcons.CustomIcon connIcon = new Textures.BlockIcons.CustomIcon(connIconPath);

    public MTEHatchInputBeamline(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, "");
    }

    public MTEHatchInputBeamline(String name, int tier, String[] desc, ITexture[][][] textures) {
        super(name, tier, desc, textures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory
                .of(activeIcon, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(connIcon) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory
                .of(sideIcon, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(connIcon) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new MTEHatchInputBeamline(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public IConnectsToBeamline getNext(IConnectsToBeamline source) {
        return null;
    }

    @Override
    public String[] getDescription() {
        return null;
    }

    public void setContents(BeamLinePacket in) {
        if (in == null) {
            this.dataPacket = null;
        } else {
            if (in.getContent()
                .getRate() > 0) {
                this.dataPacket = in;
                delay = true;
            } else {
                this.dataPacket = null;
            }
        }
    }

    @Override
    public void moveAround(IGregTechTileEntity tile) {
        if (this.delay) {
            this.delay = false;
        } else {
            this.setContents(null);
        }
    }
}
