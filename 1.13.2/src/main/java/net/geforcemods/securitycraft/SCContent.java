package net.geforcemods.securitycraft;

import net.geforcemods.securitycraft.api.TileEntitySCTE;
import net.geforcemods.securitycraft.blocks.mines.BlockMine;
import net.geforcemods.securitycraft.entity.EntityBouncingBetty;
import net.geforcemods.securitycraft.entity.EntityBullet;
import net.geforcemods.securitycraft.entity.EntityIMSBomb;
import net.geforcemods.securitycraft.entity.EntitySecurityCamera;
import net.geforcemods.securitycraft.entity.EntitySentry;
import net.geforcemods.securitycraft.entity.EntityTaserBullet;
import net.geforcemods.securitycraft.items.ItemModule;
import net.geforcemods.securitycraft.tileentity.TileEntityAlarm;
import net.geforcemods.securitycraft.tileentity.TileEntityCageTrap;
import net.geforcemods.securitycraft.tileentity.TileEntityClaymore;
import net.geforcemods.securitycraft.tileentity.TileEntityIMS;
import net.geforcemods.securitycraft.tileentity.TileEntityInventoryScanner;
import net.geforcemods.securitycraft.tileentity.TileEntityKeycardReader;
import net.geforcemods.securitycraft.tileentity.TileEntityKeypad;
import net.geforcemods.securitycraft.tileentity.TileEntityKeypadChest;
import net.geforcemods.securitycraft.tileentity.TileEntityKeypadFurnace;
import net.geforcemods.securitycraft.tileentity.TileEntityLaserBlock;
import net.geforcemods.securitycraft.tileentity.TileEntityLogger;
import net.geforcemods.securitycraft.tileentity.TileEntityMotionLight;
import net.geforcemods.securitycraft.tileentity.TileEntityOwnable;
import net.geforcemods.securitycraft.tileentity.TileEntityPortableRadar;
import net.geforcemods.securitycraft.tileentity.TileEntityProtecto;
import net.geforcemods.securitycraft.tileentity.TileEntityRetinalScanner;
import net.geforcemods.securitycraft.tileentity.TileEntityScannerDoor;
import net.geforcemods.securitycraft.tileentity.TileEntitySecretSign;
import net.geforcemods.securitycraft.tileentity.TileEntitySecurityCamera;
import net.geforcemods.securitycraft.tileentity.TileEntityTrackMine;
import net.geforcemods.securitycraft.util.Reinforced;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;

public class SCContent
{
	//Blocks
	public static Block alarm;
	public static Block bogusLavaFlowing;
	public static Block bogusWaterFlowing;
	public static Block bouncingBetty;
	public static Block cageTrap;
	public static Block claymore;
	public static Block cobblestoneMine;
	public static Block diamondOreMine;
	public static Block dirtMine;
	public static Block frame;
	public static Block furnaceMine;
	public static Block gravelMine;
	public static Block ims;
	public static Block inventoryScanner;
	public static Block inventoryScannerField;
	public static Block ironFence;
	public static Block ironTrapdoor;
	public static Block keycardReader;
	public static Block keypad;
	public static Block keypadChest;
	public static Block keypadFurnace;
	public static Block laserBlock;
	public static Block laserField;
	public static Block motionActivatedLight;
	public static Block panicButton;
	public static Block portableRadar;
	public static Block protecto;
	public static Block reinforcedDoor;
	public static Block reinforcedFencegate;
	public static Block retinalScanner;
	public static Block sandMine;
	public static Block scannerDoor;
	public static Block secretSignStanding;
	public static Block secretSignWall;
	public static Block securityCamera;
	public static Block stoneMine;
	public static Block trackMine;
	public static Block usernameLogger;
	public static BlockMine mine;
	public static BlockStaticLiquid bogusLava;
	public static BlockStaticLiquid bogusWater;

	//Reinforced Blocks (ordered by vanilla building blocks creative tab order)
	@Reinforced(hasPage=true) public static Block reinforcedStone;
	@Reinforced public static Block reinforcedGranite;
	@Reinforced public static Block reinforcedPolishedGranite;
	@Reinforced public static Block reinforcedDiorite;
	@Reinforced public static Block reinforcedPolishedDiorite;
	@Reinforced public static Block reinforcedAndesite;
	@Reinforced public static Block reinforcedPolishedAndesite;
	@Reinforced public static Block reinforcedDirt;
	@Reinforced public static Block reinforcedCobblestone;
	@Reinforced public static Block reinforcedOakPlanks;
	@Reinforced public static Block reinforcedSprucePlanks;
	@Reinforced public static Block reinforcedBirchPlanks;
	@Reinforced public static Block reinforcedJunglePlanks;
	@Reinforced public static Block reinforcedAcaciaPlanks;
	@Reinforced public static Block reinforcedDarkOakPlanks;
	@Reinforced public static Block reinforcedSand;
	@Reinforced public static Block reinforcedGravel;
	@Reinforced public static Block reinforcedOakLog;
	@Reinforced public static Block reinforcedSpruceLog;
	@Reinforced public static Block reinforcedBirchLog;
	@Reinforced public static Block reinforcedJungleLog;
	@Reinforced public static Block reinforcedAcaciaLog;
	@Reinforced public static Block reinforcedDarkOakLog;
	@Reinforced public static Block reinforcedGlass;
	@Reinforced public static Block reinforcedLapisBlock;
	@Reinforced public static Block reinforcedSandstone;
	@Reinforced public static Block reinforcedChiseledSandstone;
	@Reinforced public static Block reinforcedCutSandstone;
	@Reinforced public static Block reinforcedWhiteWool;
	@Reinforced public static Block reinforcedOrangeWool;
	@Reinforced public static Block reinforcedMagentaWool;
	@Reinforced public static Block reinforcedLightBlueWool;
	@Reinforced public static Block reinforcedYellowWool;
	@Reinforced public static Block reinforcedLimeWool;
	@Reinforced public static Block reinforcedPinkWool;
	@Reinforced public static Block reinforcedGrayWool;
	@Reinforced public static Block reinforcedLightGrayWool;
	@Reinforced public static Block reinforcedCyanWool;
	@Reinforced public static Block reinforcedPurpleWool;
	@Reinforced public static Block reinforcedBlueWool;
	@Reinforced public static Block reinforcedBrownWool;
	@Reinforced public static Block reinforcedGreenWool;
	@Reinforced public static Block reinforcedRedWool;
	@Reinforced public static Block reinforcedBlackWool;
	@Reinforced public static Block reinforcedGoldBlock;
	@Reinforced public static Block reinforcedIronBlock;
	@Reinforced public static Block reinforcedOakSlab;
	@Reinforced public static Block reinforcedSpruceSlab;
	@Reinforced public static Block reinforcedBirchSlab;
	@Reinforced public static Block reinforcedJungleSlab;
	@Reinforced public static Block reinforcedAcaciaSlab;
	@Reinforced public static Block reinforcedDarkOakSlab;
	@Reinforced public static Block reinforcedStoneSlab;
	@Reinforced public static Block reinforcedSandstoneSlab;
	@Reinforced public static Block reinforcedCobblestoneSlab;
	@Reinforced public static Block reinforcedBrickSlab;
	@Reinforced public static Block reinforcedStoneBrickSlab;
	@Reinforced public static Block reinforcedNetherBrickSlab;
	@Reinforced public static Block reinforcedQuartzSlab;
	@Reinforced public static Block reinforcedRedSandstoneSlab;
	@Reinforced public static Block reinforcedPurpurSlab;
	@Reinforced public static Block reinforcedBricks;
	@Reinforced public static Block reinforcedMossyCobblestone;
	@Reinforced public static Block reinforcedObsidian;
	@Reinforced public static Block reinforcedPurpurBlock;
	@Reinforced public static Block reinforcedPurpurPillar;
	@Reinforced public static Block reinforcedPurpurStairs;
	@Reinforced public static Block reinforcedOakStairs;
	@Reinforced public static Block reinforcedDiamondBlock;
	@Reinforced public static Block reinforcedCobblestoneStairs;
	@Reinforced public static Block reinforcedNetherrack;
	@Reinforced public static Block reinforcedGlowstone;
	@Reinforced public static Block reinforcedStoneBricks;
	@Reinforced public static Block reinforcedMossyStoneBricks;
	@Reinforced public static Block reinforcedCrackedStoneBricks;
	@Reinforced public static Block reinforcedChiseledStoneBricks;
	@Reinforced public static Block reinforcedBrickStairs;
	@Reinforced public static Block reinforcedStoneBrickStairs;
	@Reinforced public static Block reinforcedNetherBricks;
	@Reinforced public static Block reinforcedNetherBrickStairs;
	@Reinforced public static Block reinforcedEndStone;
	@Reinforced public static Block reinforcedEndStoneBricks;
	@Reinforced public static Block reinforcedSandstoneStairs;
	@Reinforced public static Block reinforcedEmeraldBlock;
	@Reinforced public static Block reinforcedSpruceStairs;
	@Reinforced public static Block reinforcedBirchStairs;
	@Reinforced public static Block reinforcedJungleStairs;
	@Reinforced public static Block reinforcedChiseledQuartz;
	@Reinforced public static Block reinforcedQuartz;
	@Reinforced public static Block reinforcedQuartzPillar;
	@Reinforced public static Block reinforcedQuartzStairs;
	@Reinforced public static Block reinforcedWhiteTerracotta;
	@Reinforced public static Block reinforcedOrangeTerracotta;
	@Reinforced public static Block reinforcedMagentaTerracotta;
	@Reinforced public static Block reinforcedLightBlueTerracotta;
	@Reinforced public static Block reinforcedYellowTerracotta;
	@Reinforced public static Block reinforcedLimeTerracotta;
	@Reinforced public static Block reinforcedPinkTerracotta;
	@Reinforced public static Block reinforcedGrayTerracotta;
	@Reinforced public static Block reinforcedLightGrayTerracotta;
	@Reinforced public static Block reinforcedCyanTerracotta;
	@Reinforced public static Block reinforcedPurpleTerracotta;
	@Reinforced public static Block reinforcedBlueTerracotta;
	@Reinforced public static Block reinforcedBrownTerracotta;
	@Reinforced public static Block reinforcedGreenTerracotta;
	@Reinforced public static Block reinforcedRedTerracotta;
	@Reinforced public static Block reinforcedBlackTerracotta;
	@Reinforced public static Block reinforcedTerracotta;
	@Reinforced public static Block reinforcedCoalBlock;
	@Reinforced public static Block reinforcedAcaciaStairs;
	@Reinforced public static Block reinforcedDarkOakStairs;
	@Reinforced(hasTint=false) public static Block reinforcedWhiteStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedOrangeStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedMagentaStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedLightBlueStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedYellowStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedLimeStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedPinkStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedGrayStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedLightGrayStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedCyanStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedPurpleStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedBlueStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedBrownStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedGreenStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedRedStainedGlass;
	@Reinforced(hasTint=false) public static Block reinforcedBlackStainedGlass;
	@Reinforced public static Block reinforcedPrismarine;
	@Reinforced public static Block reinforcedPrismarineBricks;
	@Reinforced public static Block reinforcedDarkPrismarine;
	@Reinforced public static Block reinforcedSeaLantern;
	@Reinforced public static Block reinforcedRedSandstone;
	@Reinforced public static Block reinforcedChiseledRedSandstone;
	@Reinforced public static Block reinforcedCutRedSandstone;
	@Reinforced public static Block reinforcedRedSandstoneStairs;
	@Reinforced public static Block reinforcedRedNetherBricks;
	@Reinforced public static Block reinforcedBoneBlock;
	@Reinforced public static Block reinforcedWhiteConcrete;
	@Reinforced public static Block reinforcedOrangeConcrete;
	@Reinforced public static Block reinforcedMagentaConcrete;
	@Reinforced public static Block reinforcedLightBlueConcrete;
	@Reinforced public static Block reinforcedYellowConcrete;
	@Reinforced public static Block reinforcedLimeConcrete;
	@Reinforced public static Block reinforcedPinkConcrete;
	@Reinforced public static Block reinforcedGrayConcrete;
	@Reinforced public static Block reinforcedLightGrayConcrete;
	@Reinforced public static Block reinforcedCyanConcrete;
	@Reinforced public static Block reinforcedPurpleConcrete;
	@Reinforced public static Block reinforcedBlueConcrete;
	@Reinforced public static Block reinforcedBrownConcrete;
	@Reinforced public static Block reinforcedGreenConcrete;
	@Reinforced public static Block reinforcedRedConcrete;
	@Reinforced public static Block reinforcedBlackConcrete;
	//ordered by vanilla decoration blocks creative tab order
	@Reinforced(hasPage=true, hasTint=false) public static Block reinforcedIronBars;
	@Reinforced(hasPage=true, hasTint=false) public static Block reinforcedGlassPane;
	@Reinforced public static Block reinforcedWhiteCarpet;
	@Reinforced public static Block reinforcedOrangeCarpet;
	@Reinforced public static Block reinforcedMagentaCarpet;
	@Reinforced public static Block reinforcedLightBlueCarpet;
	@Reinforced public static Block reinforcedYellowCarpet;
	@Reinforced public static Block reinforcedLimeCarpet;
	@Reinforced public static Block reinforcedPinkCarpet;
	@Reinforced public static Block reinforcedGrayCarpet;
	@Reinforced public static Block reinforcedLightGrayCarpet;
	@Reinforced public static Block reinforcedCyanCarpet;
	@Reinforced public static Block reinforcedPurpleCarpet;
	@Reinforced public static Block reinforcedBlueCarpet;
	@Reinforced public static Block reinforcedBrownCarpet;
	@Reinforced public static Block reinforcedGreenCarpet;
	@Reinforced public static Block reinforcedRedCarpet;
	@Reinforced public static Block reinforcedBlackCarpet;
	@Reinforced(hasTint=false) public static Block reinforcedWhiteStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedOrangeStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedMagentaStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedLightBlueStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedYellowStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedLimeStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedPinkStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedGrayStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedLightGrayStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedCyanStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedPurpleStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedBlueStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedBrownStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedGreenStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedRedStainedGlassPane;
	@Reinforced(hasTint=false) public static Block reinforcedBlackStainedGlassPane;
	//anything else, alphabetical
	@Reinforced public static Block reinforcedStoneStairs;

	//Items
	public static Item adminTool;
	public static Item briefcase;
	public static Item cameraMonitor;
	public static Item codebreaker;
	public static Item fLavaBucket;
	public static Item fWaterBucket;
	public static Item keycardLvl1;
	public static Item keycardLvl2;
	public static Item keycardLvl3;
	public static Item keycardLvl4;
	public static Item keycardLvl5;
	public static Item keyPanel;
	public static Item limitedUseKeycard;
	public static Item reinforcedDoorItem;
	public static Item remoteAccessMine;
	public static Item scannerDoorItem;
	public static Item scManual;
	public static Item secretSignItem;
	public static Item sentry;
	public static Item taser;
	public static Item taserPowered;
	public static Item universalBlockModifier;
	public static Item universalBlockReinforcerLvL1;
	public static Item universalBlockReinforcerLvL2;
	public static Item universalBlockReinforcerLvL3;
	public static Item universalBlockRemover;
	public static Item universalKeyChanger;
	public static Item universalOwnerChanger;
	public static Item wireCutters;

	//Modules
	public static ItemModule blacklistModule;
	public static ItemModule disguiseModule;
	public static ItemModule harmingModule;
	public static ItemModule redstoneModule;
	public static ItemModule smartModule;
	public static ItemModule storageModule;
	public static ItemModule whitelistModule;

	//Tile entity typses
	public static TileEntityType<TileEntityOwnable> teTypeOwnable;
	public static TileEntityType<TileEntitySCTE> teTypeAbstract;
	public static TileEntityType<TileEntityKeypad> teTypeKeypad;
	public static TileEntityType<TileEntityLaserBlock> teTypeLaserBlock;
	public static TileEntityType<TileEntityCageTrap> teTypeCageTrap;
	public static TileEntityType<TileEntityKeycardReader> teTypeKeycardReader;
	public static TileEntityType<TileEntityInventoryScanner> teTypeInventoryScanner;
	public static TileEntityType<TileEntityPortableRadar> teTypePortableRadar;
	public static TileEntityType<TileEntitySecurityCamera> teTypeSecurityCamera;
	public static TileEntityType<TileEntityLogger> teTypeUsernameLogger;
	public static TileEntityType<TileEntityRetinalScanner> teTypeRetinalScanner;
	public static TileEntityType<TileEntityKeypadChest> teTypeKeypadChest;
	public static TileEntityType<TileEntityAlarm> teTypeAlarm;
	public static TileEntityType<TileEntityClaymore> teTypeClaymore;
	public static TileEntityType<TileEntityKeypadFurnace> teTypeKeypadFurnace;
	public static TileEntityType<TileEntityIMS> teTypeIms;
	public static TileEntityType<TileEntityProtecto> teTypeProtecto;
	public static TileEntityType<TileEntityScannerDoor> teTypeScannerDoor;
	public static TileEntityType<TileEntitySecretSign> teTypeSecretSign;
	public static TileEntityType<TileEntityMotionLight> teTypeMotionLight;
	public static TileEntityType<TileEntityTrackMine> teTypeTrackMine;

	//Entity types
	public static EntityType<EntityBouncingBetty> eTypeBouncingBetty;
	public static EntityType<EntityTaserBullet> eTypeTaserBullet;
	public static EntityType<EntityIMSBomb> eTypeImsBomb;
	public static EntityType<EntitySecurityCamera> eTypeSecurityCamera;
	public static EntityType<EntitySentry> eTypeSentry;
	public static EntityType<EntityBullet> eTypeBullet;
}
