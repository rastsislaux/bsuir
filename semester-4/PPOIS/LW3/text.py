import pygame


class Text:

    def __init__(self, text, fontsize, rectcenter, textcolor = (255, 255, 255)):
        self.font = pygame.font.Font("fonts/emulogic.ttf", fontsize)
        self.text = self.font.render(text, False, textcolor)
        self.rect = self.text.get_rect(center=rectcenter)
        self.y_offset = 0

    def update(self, game):
        self.rect.y -= 1
        self.y_offset -= 1

        if self.y_offset == -100:
            game.world.remove_text(self)

    def render(self, game):
        game.screen.blit(self.text, self.rect)

    def render_in_game(self, game):
        game.screen.blit(self.text, game.world.camera.apply(self))
