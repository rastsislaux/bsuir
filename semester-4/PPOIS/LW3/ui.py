import pygame.font


class GameUI:
    
    def __init__(self):
        self.font = pygame.font.Font("fonts/emulogic.ttf", 20)
        self.text = "SCORE COINS WORLD TIME LIVES"
        
    def render(self, game):
        x = 10
        for word in self.text.split(' '):
            rect = self.font.render(word, False, (255, 255, 255))
            game.screen.blit(rect, (x, 0))
            x += 168

        text = self.font.render(str(game.world.player.score), False, (255, 255, 255))
        rect = text.get_rect(center=(60, 35))
        game.screen.blit(text, rect)

        text = self.font.render(str(game.world.player.coins), False, (255, 255, 255))
        rect = text.get_rect(center=(230, 35))
        game.screen.blit(text, rect)

        text = self.font.render(game.world.name, False, (255, 255, 255))
        rect = text.get_rect(center=(395, 35))
        game.screen.blit(text, rect)

        text = self.font.render(str(game.world.time), False, (255, 255, 255))
        rect = text.get_rect(center=(557, 35))
        game.screen.blit(text, rect)

        text = self.font.render(str(game.world.player.num_of_lives), False, (255, 255, 255))
        rect = text.get_rect(center=(730, 35))
        game.screen.blit(text, rect)
