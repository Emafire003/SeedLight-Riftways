package me.emafire003.dev.seedlight_riftways.items;

import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//It is going to be an item which whill have some data assigned to it. The data is composed of
// server ip. And maybe server name? The data gets assigned in the anvil maybe.

//it is a leaf which sprouted from another seed, so it can point back to that seed. So, it is
//kind of a coordiante system.
//maybe like a lodestone compass?
//Problem is how to assign the value form in-game. Maybe a new block like riftwaylocator? But i'd need a screen for that


//The seed item may be either found in the end cities or crafted with 2 beacons a nether star and seeds
public class InterRiftwaysLeafItem extends Item {

    public InterRiftwaysLeafItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient){
            SeedLightRiftwaysClient.connetToOtherServer();
        }
        return TypedActionResult.fail(ItemStack.EMPTY);
    }

    private static final AtomicInteger CONNECTOR_THREADS_COUNT = new AtomicInteger(0);

    /*private void connect(MinecraftClient client, ServerAddress address) {
        final CompletableFuture<Optional<PlayerPublicKey.PublicKeyData>> completableFuture = client.getProfileKeys().refresh();
        LOGGER.info("Connecting to {}, {}", address.getAddress(), address.getPort());
        Thread thread = new Thread("Server Connector #" + CONNECTOR_THREADS_COUNT.incrementAndGet()) {
            public void run() {
                InetSocketAddress inetSocketAddress = null;

                try {
                    if (ConnectScreen.this.connectingCancelled) {
                        return;
                    }

                    Optional<InetSocketAddress> optional = AllowedAddressResolver.DEFAULT.resolve(address).map(Address::getInetSocketAddress);
                    if (ConnectScreen.this.connectingCancelled) {
                        return;
                    }

                    if (!optional.isPresent()) {
                        client.execute(() -> {
                            LOGGER.info("!!!!!!!!!!!!!!!!! YOU HAVE BEEN DISCOONNECTED !!!!!!!!!!!");
                            //client.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, ScreenTexts.CONNECT_FAILED, ConnectScreen.BLOCKED_HOST_TEXT));
                        });
                        return;
                    }

                    inetSocketAddress = (InetSocketAddress)optional.get();
                    ClientConnection connection = ClientConnection.connect(inetSocketAddress, client.options.shouldUseNativeTransport());
                    connection.setPacketListener(new ClientLoginNetworkHandler(connection, client, ConnectScreen.this.parent, ConnectScreen.this::setStatus));
                    connection.send(new HandshakeC2SPacket(inetSocketAddress.getHostName(), inetSocketAddress.getPort(), NetworkState.LOGIN));
                    connection.send(new LoginHelloC2SPacket(client.getSession().getUsername(), (Optional)completableFuture.join(), Optional.ofNullable(client.getSession().getUuidOrNull())));
                } catch (Exception var6) {
                    if (ConnectScreen.this.connectingCancelled) {
                        return;
                    }

                    Throwable var5 = var6.getCause();
                    Exception exception3;
                    if (var5 instanceof Exception) {
                        Exception exception2 = (Exception)var5;
                        exception3 = exception2;
                    } else {
                        exception3 = var6;
                    }

                    LOGGER.error("Couldn't connect to server", var6);
                    String string = inetSocketAddress == null ? exception3.getMessage() : exception3.getMessage().replaceAll(inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort(), "").replaceAll(inetSocketAddress.toString(), "");
                    client.execute(() -> {
                        LOGGER.info("!!!!!!!!!!!!!!!!! YOU HAVE BEEN DISCOONNECTED !!!!!!!!!!!");
                        //client.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, ScreenTexts.CONNECT_FAILED, Text.translatable("disconnect.genericReason", new Object[]{string})));
                    });
                }

            }
        };
        thread.setUncaughtExceptionHandler(new UncaughtExceptionLogger(LOGGER));
        thread.start();
    }*/

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(!Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("This is a very magical thing indeed"));
        } else {
            tooltip.add(Text.translatable("item.lightwithin.luxintus_berry.tooltip"));
            tooltip.add(Text.translatable("item.lightwithin.luxintus_berry.tooltip1"));
        }
    }
}
