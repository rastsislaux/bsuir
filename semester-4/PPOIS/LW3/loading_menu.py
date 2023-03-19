import pygame.time

from const import WINDOW_WIDTH, WINDOW_HEIGHT
from menu_manager import STATE_GAME, STATE_MAIN_MENU
from text import Text


class LoadingMenu:

    def __init__(self, game):
        self.time = pygame.time.get_ticks()
        self.loading_type = True
        self.bg = pygame.Surface((WINDOW_WIDTH, WINDOW_HEIGHT))
        self.text = Text("WORLD " + game.world.name, 32, (WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2))

    def update(self, game):
        if pygame.time.get_ticks() >= self.time + (5250 if not self.loading_type else 2500):
            if self.loading_type:
                game.menu_manager.current_state = STATE_GAME
                game.sound_manager.play("overworld", 999999, 0.5)
                game.world.in_event = False
            else:
                game.menu_manager.current_state = STATE_MAIN_MENU
                self.set_text_and_type("WORLD " + game.world.name, True)
                game.world.reset(True)

    def set_text_and_type(self, text, type):
        self.text = Text(text, 32, (WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2))
        self.loading_type = type

    def render(self, game):
        game.screen.blit(self.bg, (0, 0))
        self.text.render(game)

    def update_time(self):
        self.time = pygame.time.get_ticks()
