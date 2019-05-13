#ifndef VIRTUALMOUSE_H
#define VIRTUALMOUSE_H

#include <QObject>

class VirtualMouse : public QObject
{
    Q_OBJECT
public:
    explicit VirtualMouse(QObject *parent = nullptr);

    void absoluteMove(qint32 x, qint32 y);
    void move(qint32 dx, qint32 dy);
    void drag(qint32 dx, qint32 dy);
    void rightClick();
    void rightButtonDown();
    void rightButtonUp();
    void leftClick();
    void leftButtonDown();
    void leftButtonUp();
    void verticalScroll(qint32 dy);

signals:

public slots:
};

#endif // VIRTUALMOUSE_H
