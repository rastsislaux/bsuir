from os import environ

import pygame
from pygame.locals import KEYDOWN

from menu_manager import MenuManager
from const import WINDOW_HEIGHT, WINDOW_WIDTH, FPS, SDL_VIDEO_CENTERED, STATE_GAME
from sound_manager import SoundManager
from world import World


class Game:

    def __init__(self):
        environ[SDL_VIDEO_CENTERED] = "1"
        pygame.mixer.pre_init(44100, -16, 2, 1024)
        pygame.init()

        pygame.display.set_caption("Super Lario Sisters")
        pygame.display.set_mode((WINDOW_WIDTH, WINDOW_HEIGHT))

        self.screen = pygame.display.set_mode((WINDOW_WIDTH, WINDOW_HEIGHT))
        self.clock = pygame.time.Clock()

        self.world = World("1-1")
        self.sound_manager = SoundManager()
        self.menu_manager = MenuManager(self)

        self.is_running = True
        self.keyR = False
        self.keyL = False
        self.keyU = False
        self.keyD = False
        self.keyShift = False

    def main_loop(self):
        while self.is_running:
            self.input()
            self.update()
            self.render()
            self.clock.tick(FPS)

    def input(self):
        if self.menu_manager.current_state == STATE_GAME:
            self.game_input()
        else:
            self.menu_input()

    def game_input(self):
        for e in pygame.event.get():

            if e.type == pygame.QUIT:
                self.is_running = False

            elif e.type == pygame.locals.KEYDOWN:
                match e.key:
                    case pygame.locals.K_RIGHT:
                        self.keyR = True
                    case pygame.locals.K_LEFT:
                        self.keyL = True
                    case pygame.locals.K_DOWN:
                        self.keyD = True
                    case pygame.locals.K_UP:
                        self.keyU = True
                    case pygame.locals.K_LSHIFT:
                        self.keyShift = True

            elif e.type == pygame.locals.KEYUP:
                match e.key:
                    case pygame.locals.K_RIGHT:
                        self.keyR = False
                    case pygame.locals.K_LEFT:
                        self.keyL = False
                    case pygame.locals.K_DOWN:
                        self.keyD = False
                    case pygame.locals.K_UP:
                        self.keyU = False
                    case pygame.locals.K_LSHIFT:
                        self.keyShift = False

    def menu_input(self):
        for e in pygame.event.get():
            if e.type == pygame.QUIT:
                self.is_running = False

            elif e.type == pygame.locals.KEYDOWN:
                if e.key == pygame.locals.K_RETURN:
                    self.menu_manager.start_loading()

    def update(self):
        self.menu_manager.update(self)

    def render(self):
        self.menu_manager.render(self)
