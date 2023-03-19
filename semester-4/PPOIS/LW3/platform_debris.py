import pygame

from const import GRAVITY, FALL_MULTIPLIER


class PlatformDebris:

    def __init__(self, x_pos, y_pos):
        self.image = pygame.image.load('images/block_debris0.png').convert_alpha()

        # 4 different parts
        self.rectangles = [
            pygame.Rect(x_pos - 20, y_pos + 16, 16, 16),
            pygame.Rect(x_pos - 20, y_pos - 16, 16, 16),
            pygame.Rect(x_pos + 20, y_pos + 16, 16, 16),
            pygame.Rect(x_pos + 20, y_pos - 16, 16, 16)
        ]
        self.y_vel = -4
        self.rect = None

    def update(self, game):
        self.y_vel += GRAVITY * FALL_MULTIPLIER

        for i in range(len(self.rectangles)):
            self.rectangles[i].y += self.y_vel
            if i < 2:
                self.rectangles[i].x -= 1
            else:
                self.rectangles[i].x += 1

        if self.rectangles[1].y > game.world.mapSize[1] * 32:
            game.world.debris.remove(self)

    def render(self, game):
        for rect in self.rectangles:
            self.rect = rect
            game.screen.blit(self.image, game.world.camera.apply(self))
