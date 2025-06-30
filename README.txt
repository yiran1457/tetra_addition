//a example js in startup
StartupEvents.registry('item', event => {
  event.create("yi:tetra_test", "basic_tetra")
    .addMajorModuleKey("yi/major_module_key", 1, 15)
    .addMajorModuleKey("yi/major_module_key2", -10, 15, true)
    .addMinorModuleKey("yi/minor_module_key", -5, -6)
    .setSlotType("head")
    .setAttributeMode("effect_armor")
})