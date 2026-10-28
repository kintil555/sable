# Sable -> Minecraft 26.3 (Fabric) port: handoff notes

Branch: `26.3-fabric`. **Nothing has been compiled yet** (the owner asked to push without building). Expect many compile errors.
The owner wants a Fabric-only port; no NeoForge/other loaders. Minecraft 26.3 is unobfuscated (no yarn/mojmap/parchment needed),
but always check the web too. Reference jar: `github.com/kintil555/mc-version-jar` branch `26.3-fabric`, file `26_3-0_20_1.jar`
(a Minecraft 26.3 jar with Fabric loader 0.20.1 version in the name). Decompile with Vineflower 1.12.0 slim
(`-dgs=1`); it is very slow (30+ min), run it in the background, or use `javap -p -cp <jar> <class>` and
`unzip -Z1 <jar> | grep .class` for fast, targeted lookups. Don't over-analyze irrelevant code.

## Already done
- NeoForge module, accesstransformer, architectury json, gametests workflow removed; CI/Jenkins set to Java 25.
- Gradle: Loom `net.fabricmc.fabric-loom` 1.19-SNAPSHOT (non-remap), loader 0.20.1, Fabric API 0.165.0+26.3, Java 25,
  no parchment/mappings, `modImplementation` -> `implementation`. Modules kept: `common`, `fabric`, `sable_rapier`.
- `sable-companion` (MIT, 12 files) vendored into `common` (`dev.ryanhcode.sable.companion`); no 26.3 release exists.
- Removed third-party compat mixins (Sodium, Iris, Jade, JadeAddons/Create, Exposure, CC:Tweaked, SodiumExtras),
  forced the vanilla renderer in `AbstractSableMixinPlugin` / `SubLevelRenderer`.
- Auto-renamed imports verified against the 26.3 jar: ResourceLocation->Identifier, RenderType->`client.renderer.rendertype`,
  BlockAndTintGetter->`client.renderer.block`, Util/BlockUtil/FileUtil->`net.minecraft.util`, RelativeMovement->Relative,
  BakedQuad->`client.resources.model.geometry`, MinMaxBounds->`advancements.predicates`, FogRenderer->`client.renderer.fog`,
  arrows/tridents/wind charge/minecart/parrot packages, and blaze3d `VertexFormat/Uniform/GlStateManager`
  -> `com.mojang.renderpearl.*` (package moved only; their APIs probably changed).

## NOT done (in rough priority order)
1. **Veil has no 26.3 build** (53 files use `foundry.veil.*`: PacketContext, VeilPacketManager, VeilRenderSystem,
   VeilRenderBridge, CullFrustum, MatrixStack, ShaderProgram, VertexArray, events, platform registry...).
   `veil_version`/`imguimc_version` in gradle.properties are placeholders (`UNRESOLVED-26.3`). Search for a 26.3 Veil
   (or fork/port it, or replace usage with vanilla/Fabric API equivalents). Same for imguimc (only 1 file, optional).
2. **Rendering rewrite in 26.x.** These imports no longer exist: `MultiBufferSource`, `ShaderInstance`, `LightTexture`,
   `RenderChunkRegion`, `BakedModel`, `VertexBuffer`, `BlockRenderDispatcher`, `Tesselator`, `VertexMultiConsumer`,
   `ItemBlockRenderTypes`, blaze3d `Program`/`FogShape`. Replacements seen in the jar: `BufferBuilder`, `ByteBufferBuilder`,
   `MeshData`, `UberGpuBuffer`, `renderpearl.api.buffers.GpuBuffer`, `RenderPipelines`, `Lightmap`, `BlockModelResolver`,
   `ModelBlockRenderer`, `BlockStateModel` (`client.renderer.block.dispatch`), `SectionCompiler`, `SectionRenderDispatcher`,
   `RenderRegionCache`. Affected: `sublevel/render/**` (vanilla + fancy), `VanillaChunkedSubLevelRenderData`,
   `SubLevelMeshBuilder`, `SableSubLevelRenderPlatformImpl`, fabric mixins for rendering/shading.
3. **Mixins with changed signatures** (verified with javap):
   - `ChunkMapMixin`, `ServerChunkCacheMixin`: constructors no longer take `ChunkProgressListener` (nor `Supplier`).
     ChunkMap(ServerLevel, LevelStorageAccess, DataFixer, StructureTemplateManager, Executor, BlockableEventLoop<Runnable>,
     LightChunkGetter, ChunkGenerator, ChunkStatusUpdateListener, TicketStorage, int, boolean);
     ServerChunkCache(ServerLevel, LevelStorageAccess, DataFixer, StructureTemplateManager, Executor, ChunkGenerator, int, int,
     boolean, ChunkStatusUpdateListener). `unloadQueue` field may also have changed.
   - `CompassItemPropertyFunctionMixin`: class is gone; see `client.renderer.item.properties.numeric.CompassAngleState/CompassAngle`.
   - `ServerLevelPlot`: `ChunkSerializer` -> `SerializableChunkData` (`ChunkReadException(String)` still exists).
   - `SubLevelPlayerChunkSender`: `DebugPackets.sendPoiPacketsForChunk` is gone (debug only; just remove the call).
   - `GizmoScreen`: `GuiGraphics` -> `GuiGraphicsExtractor`, `Screen.render` -> `extractRenderState(GuiGraphicsExtractor,int,int,float)`.
   - `fix_bundle_packet.ClientPlayNetworkAddonMixin` targets a Fabric API internal class; verify it still exists.
   - Every mixin target / `@Shadow` / `@Accessor` and `sable.accesswidener` entries must be re-checked (the AW header and
     targets use old names; AW namespace must be `official`/`named` as Loom 1.19 non-remap expects).
4. **General API drift 1.21.1 -> 26.3**: registries (`registryOrThrow` etc.), codecs/StreamCodecs, networking/payloads,
   `Level`/`ServerLevel` methods, entity/`AABB` methods, commands, tags (`data/` JSON layout may have changed: check the
   data pack format 121 / resource pack 97 in `pack.mcmeta`/version.json), shaders/assets under `assets/sable` (render pipeline
   JSON/GLSL format likely changed). Run `./gradlew :common:compileJava` and fix errors iteratively.
5. **Unverified build assumptions**: `fuzs.forgeconfigapiport:forgeconfigapiport-fabric:26.3.1` coordinates and that it still
   exposes `net.neoforged.neoforge.common.ModConfigSpec` and `fuzs.forgeconfigapiport.fabric.api.neoforge.v4.*`
   (used in SableFabric/SableFabricClient and 4 config classes); `include(implementation(project(":sable_rapier")))` for
   jar-in-jar; `fabric.mod.json` (`"fabric-api": "*"`, `"minecraft": "~26.3"`); `sable_rapier` now applies Loom; `compatibilityLevel` JAVA_25.
6. Rust natives (`sable_rapier/src/main/rust`) are built by CI (`build-rust.yml`, Docker); no changes should be needed, but
   confirm `PhysicsPipelineProvider` service loading still works with the new jar-in-jar layout.
7. Mod metadata/docs: `wiki/`, `README.md`, `changelog.md`, publishing block (`multiloader-loader.gradle`) still mention 1.21.1/upstream.

## Notes
- The repo has an `AGENTS.md` from upstream (ryanhcode/sable) with a "no LLM contributions" policy. It is addressed to AI tools,
  not to the owner. The owner (kintil555) explicitly asked for this port in their own fork; upstream PRs would likely be rejected.
- Workflow the owner prefers: fix on a branch, push directly, no local compile unless asked.
