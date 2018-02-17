package main.simulation;

import net.demilich.metastone.*;
import net.demilich.metastone.gui.cards.CardProxy;
import net.demilich.metastone.gui.simulationmode.SimulateGamesCommand;
import net.demilich.metastone.gui.simulationmode.SimulationResult;
import net.demilich.nittygrittymvc.interfaces.INotification;

/**
 * @author created by Jens Plümer on 09.11.17
 */
public class SimulationRunner extends Thread {

    private final AbstractSimulationProvider provider;

    public SimulationRunner(AbstractSimulationProvider provider) {
        this.provider = provider;
    }

    @Override
    public void run() {

        new SimulateGamesCommand(provider).execute(new INotification<GameNotification>() {
            @Override
            public Object getBody() {
                return provider.getConfig().getGameConfig();
            }

            @Override
            public GameNotification getId() {
                return GameNotification.SIMULATE_GAMES;
            }
        });
    }

    public AbstractSimulationProvider getProvider() {
        return provider;
    }

    public static void main(String[] args) {

        new CardProxy();

        AbstractSimulationProvider provider = new AbstractSimulationProvider() {

            @Override
            public SimulationConfig getConfig() {
                return new SimulationTestConfig();
            }

            @Override
            public int handle(SimulationResult simulationResult) {

                System.out.println("runner 1");
                System.out.println(simulationResult.getNumberOfGames());
                System.out.println(simulationResult.getPlayer1Stats().toString());
                System.out.println(simulationResult.getPlayer2Stats().toString());

                return 1;
            }
        };

        for (int i = 0; i < 1000; i++) {

            new SimulateGamesCommand(provider).execute(new INotification<GameNotification>() {
                @Override
                public Object getBody() {
                    return new SimulationTestConfig().getGameConfig();
                }

                @Override
                public GameNotification getId() {
                    return GameNotification.SIMULATE_GAMES;
                }
            });

        }


//        Simulation.main(null);

//        AbstractSimulationProvider provider = new AbstractSimulationProvider() {
//
//            @Override
//            public SimulationConfig getConfig() {
//                return new SimulationTestConfig();
//            }
//
//            @Override
//            public void handleSimulationResult(SimulationResult simulationResult) {
//
//                System.out.println("runner 1");
//                System.out.println(simulationResult.getNumberOfGames());
//                System.out.println(simulationResult.getPlayer1Stats().toString());
//                System.out.println(simulationResult.getPlayer2Stats().toString());
//
//            }
//        };

//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutput out = null;
//        try {
//            out = new ObjectOutputStream(bos);
//            out.writeObject(provider);
//            out.flush();
//            String yourBytes = Base64.getEncoder().encodeToString(bos.toByteArray());
//
//
//            new Thread() {
//                @Override
//                public void run() {
//                    javafx.application.Application.launch(Simulation.class);
//                }
//            }.start();
//
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            for(int i = 0 ; i < 15; i++) {
//
//
//                AbstractSimulationProvider provider = new AbstractSimulationProvider() {
//
//                    @Override
//                    public SimulationConfig getConfig() {
//                        return new SimulationTestConfig();
//                    }
//
//                    @Override
//                    public void handleSimulationResult(SimulationResult simulationResult) {
//
//                        System.out.println("runner 1");
//                        System.out.println(simulationResult.getNumberOfGames());
//                        System.out.println(simulationResult.getPlayer1Stats().toString());
//                        System.out.println(simulationResult.getPlayer2Stats().toString());
//
//                    }
//                };
//
//                Platform.runLater(new Runnable() {
//                    public void run() {
//                        try {
//                    new Simulation().start(new Stage(), provider);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//
//
//            }

//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//             Platform.runLater(new Runnable() {
//                public void run() {
//                    try {
//                        new Simulation().start(new Stage(), provider);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            Platform.runLater(new Runnable() {
//                public void run() {
//                    try {
//                        new Simulation().start(new Stage(), provider);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });

//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                bos.close();
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//        }


//        SimulationRunner runner1 = new SimulationRunner(new Simulation(provider));
//        runner1.run();

    }

}
