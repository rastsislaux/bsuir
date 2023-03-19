import pygame


class CoinDebris:
    
    def __init__(self, x, y):
        self.rect = pygame.Rect(x, y, 16, 28)
        
        self.y_vel = -2
        self.y_offset = 0
        self.moving_up = True

        self.current_image = 0
        self.image_tick = 0
        self.images = [
            pygame.image.load('images/coin_an0.png').convert_alpha(),
            pygame.image.load('images/coin_an1.png').convert_alpha(),
            pygame.image.load('images/coin_an2.png').convert_alpha(),
            pygame.image.load('images/coin_an3.png').convert_alpha()
        ]
        
    def update(self, game):
        self.image_tick += 1

        if self.image_tick % 15 == 0:
            self.current_image += 1

        if self.current_image == 4:
            self.current_image = 0
            self.image_tick = 0

        if self.moving_up:
            self.y_offset += self.y_vel
            self.rect.y += self.y_vel
            if self.y_offset < -50:
                self.moving_up = False
                self.y_vel = -self.y_vel
        else:
            self.y_offset += self.y_vel
            self.rect.y += self.y_vel
            if self.y_offset == 0:
                game.world.debris.remove(self)

    def render(self, game):
        game.screen.blit(self.images[self.current_image], game.world.camera.apply(self))
        