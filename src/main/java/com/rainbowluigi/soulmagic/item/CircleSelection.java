package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public interface CircleSelection {

    public List<CircleSelectionEntry> getEntries(ItemStack stack);

    public void onSelection(int index, ItemStack stack);

    public static class CircleSelectionEntry {
        private Text name;
        private UpgradeSprite sprite;

        public CircleSelectionEntry(Text name, UpgradeSprite sprite) {
            this.name = name;
            this.sprite = sprite;
        }

        public Text getName() {
            return this.name;
        }

        public UpgradeSprite getSprite() {
            return this.sprite;
        }
    }
}