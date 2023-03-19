import pygame

from const import WINDOW_WIDTH, WINDOW_HEIGHT
from text import Text


class MainMenu:

    def __init__(self):
        self.main_image = pygame.image.load(r"images/logo.png").convert_alpha()
        self.start_text = Text("Press ENTER to start", 16, (WINDOW_WIDTH - WINDOW_WIDTH * 0.72, WINDOW_HEIGHT - WINDOW_HEIGHT * 0.3))

    def render(self, game):
        game.screen.blit(self.main_image, (50, 50))
        self.start_text.render(game)
