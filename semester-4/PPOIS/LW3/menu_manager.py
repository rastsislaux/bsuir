import pygame.display

from const import STATE_MAIN_MENU, STATE_LOADING, STATE_GAME
from loading_menu import LoadingMenu
from main_menu import MainMenu


class MenuManager:

    def __init__(self, game):
        self.current_state = STATE_MAIN_MENU

        self.main_menu = MainMenu()
        self.loading_menu = LoadingMenu(game)

    def update(self, game):
        if self.current_state == STATE_MAIN_MENU:
            pass

        elif self.current_state == STATE_LOADING:
            self.loading_menu.update(game)

        elif self.current_state == STATE_GAME:
            game.world.update(game)

    def render(self, game):
        if self.current_state == STATE_MAIN_MENU:
            game.world.render_world(game)
            self.main_menu.render(game)

        elif self.current_state == STATE_LOADING:
            self.loading_menu.render(game)

        elif self.current_state == STATE_GAME:
            game.world.render(game)

        pygame.display.update()

    def start_loading(self):
        self.current_state = STATE_LOADING
        self.loading_menu.update_time()
