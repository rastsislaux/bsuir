import pygame


class SoundManager:

    def __init__(self):
        self.sounds: dict[str, pygame.mixer.Sound] = {}
        self.load_sounds()

    def load_sounds(self):
        self.sounds['overworld'] = pygame.mixer.Sound('sounds/overworld.wav')
        self.sounds['overworld_fast'] = pygame.mixer.Sound('sounds/overworld-fast.wav')
        self.sounds['level_end'] = pygame.mixer.Sound('sounds/levelend.wav')
        self.sounds['coin'] = pygame.mixer.Sound('sounds/coin.wav')
        self.sounds['small_mario_jump'] = pygame.mixer.Sound('sounds/jump.wav')
        self.sounds['big_mario_jump'] = pygame.mixer.Sound('sounds/jumpbig.wav')
        self.sounds['brick_break'] = pygame.mixer.Sound('sounds/blockbreak.wav')
        self.sounds['block_hit'] = pygame.mixer.Sound('sounds/blockhit.wav')
        self.sounds['mushroom_appear'] = pygame.mixer.Sound('sounds/mushroomappear.wav')
        self.sounds['mushroom_eat'] = pygame.mixer.Sound('sounds/mushroomeat.wav')
        self.sounds['death'] = pygame.mixer.Sound('sounds/death.wav')
        self.sounds['pipe'] = pygame.mixer.Sound('sounds/pipe.wav')
        self.sounds['kill_mob'] = pygame.mixer.Sound('sounds/kill_mob.wav')
        self.sounds['game_over'] = pygame.mixer.Sound('sounds/gameover.wav')
        self.sounds['scorering'] = pygame.mixer.Sound('sounds/scorering.wav')
        self.sounds['fireball'] = pygame.mixer.Sound('sounds/fireball.wav')
        self.sounds['shot'] = pygame.mixer.Sound('sounds/shot.wav')

    def play(self, name, loop, volume):
        self.sounds[name].play(loops=loop)
        self.sounds[name].set_volume(volume)

    def stop(self, name):
        self.sounds[name].stop()

    def start_fast_music(self, game):
        if game.world.name == '1-1':
            self.stop('overworld')
            self.play('overworld_fast', 99999, 0.5)
