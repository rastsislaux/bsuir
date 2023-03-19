import pygame

from const import WINDOW_HEIGHT, WINDOW_WIDTH


class Camera:

    def __init__(self, width, height):
        self.rect = pygame.Rect(0, 0, width, height)
        self.complex_camera(self.rect)

    def complex_camera(self, target_rect):
        x, y = target_rect.x, target_rect.y
        width, height = self.rect.width, self.rect.height
        x, y = (-x + WINDOW_WIDTH / 2 - target_rect.width / 2), (-y + WINDOW_HEIGHT / 2 - target_rect.height)

        x = min(0, x)
        x = max(-(self.rect.width - WINDOW_WIDTH), x)
        y = WINDOW_HEIGHT - self.rect.h

        return pygame.Rect(x, y, width, height)

    def apply(self, target):
        return target.rect.x + self.rect.x, target.rect.y

    def update(self, target):
        self.rect = self.complex_camera(target)

    def reset(self):
        self.rect = pygame.Rect(0, 0, self.rect.w, self.rect.h)
