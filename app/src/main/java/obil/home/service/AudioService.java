package obil.home.service;

public interface AudioService {
    void stop();
    void play(String file);

    void playIfStopped(String file);

    void postConstruct();
}
