本mod并未向游戏内添加任何物品，<br>
而是添加了kjs的兼容，<br>
允许kjs来注册tetra的模块化物品，<br>
有人因为我的mod注册的物品可以装备于装备栏，<br>
而认为我的mod抄袭了tetrawear<br>
于是我只能将这个仍然处于开发中，<br>
且将移至另一个mod的代码上传<br>
(I don't speak English. I used AI for this.)<br>
This mod does not introduce any new items into the game. <br>
Its purpose is to add compatibility with KJS, <br>
enabling KJS to register modular items from Tetra.<br>
Unfortunately, some individuals have accused this mod of plagiarizing Tetrawear, <br>
due to the fact that items registered via this mod can be equipped in equipment slots.<br>
Consequently, I was forced to upload this code,<br>
which remains in development and is intended to be relocated to another mod in the future.<br>
```js
//a example js in startup
StartupEvents.registry('item', event => {
  event.create("yi:tetra_test", "basic_tetra")
    .addMajorModuleKey("yi/major_module_key", 1, 15)
    .addMajorModuleKey("yi/major_module_key2", -10, 15, true)
    .addMinorModuleKey("yi/minor_module_key", -5, -6)
    .setSlotType("head")
    .setAttributeMode("effect_armor")
})
```