# Sable 26.3 Fabric port (in progress)

Done: NeoForge removed, Loom 1.19-SNAPSHOT (unobfuscated, no mappings), Fabric Loader 0.20.1, Fabric API 0.165.0+26.3, Java 25,
sable-companion vendored (no 26.3 release), Sodium/Iris/Jade/Exposure/CC:Tweaked compat mixins removed, moved MC classes renamed
(ResourceLocation -> Identifier, RenderType, AbstractArrow, etc.).

Blockers / TODO (not compiled yet):
- Veil has no 26.3 release (53 files depend on it); `veil_version` / `imguimc_version` are placeholders in gradle.properties.
- Rendering rewritten in 26.x (ShaderInstance, MultiBufferSource, LightTexture, BakedModel, VertexBuffer, RenderChunkRegion,
  BlockRenderDispatcher, Tesselator, GuiGraphics -> GuiGraphicsExtractor): sublevel render code must be reworked.
- Mixins to update: ChunkMapMixin / ServerChunkCacheMixin (ChunkProgressListener param removed from constructors),
  CompassItemPropertyFunctionMixin (now CompassAngleState), ChunkSerializer -> SerializableChunkData, DebugPackets removed.
- Registries/codec/network API changes elsewhere need a compile pass.
